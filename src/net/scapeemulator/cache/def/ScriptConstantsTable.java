package net.scapeemulator.cache.def;

import java.nio.ByteBuffer;

import net.scapeemulator.cache.util.ByteBufferUtils;

public class ScriptConstantsTable {

    public int anInt3658;
    public int anInt3662;

    public NodeTable nodeTable;

    private NodeTable nodeTable2;
    public String stringValue;
    public int intValue;

    public final void readDefinition(ByteBuffer buffer) {
        while (true) {
            int opcode = buffer.get();
            if (opcode == 0) {
                return;
            }
            decodeOpcode(opcode, buffer);
        }
    }

    private final void decodeOpcode(int opcode, ByteBuffer stream) {
        if (1 == opcode) {
            anInt3662 = stream.get();
        } else if (2 == opcode) {
            anInt3658 = stream.get();
        } else if (3 == opcode) {
            stringValue = ByteBufferUtils.getJagexString(stream);
        } else if (4 == opcode) {
            intValue = stream.getInt();
        } else if (5 == opcode || 6 == opcode) {
            int count = stream.getShort();
            nodeTable = new NodeTable(estimateNeededBuckets(count));
            for (int i = 0; i < count; ++i) {
                int key = stream.getInt();
                Node node;
                if (opcode == 5) {
                    node = new StringNode(ByteBufferUtils.getJagexString(stream));
                } else {
                    node = new IntegerNode(stream.getInt());
                }

                nodeTable.put(node, (long) key);
            }
        }
    }

    static final int estimateNeededBuckets(int count) {
        --count;
        count |= count >>> 1;
        count |= count >>> 2;
        count |= count >>> 4;
        count |= count >>> 8;
        count |= count >>> 16;
        return 1 + count;
    }

    final String getStringValue(int key) {
        if (null == nodeTable) {
            return stringValue;
        }

        StringNode node = (StringNode) nodeTable.get((long) key);
        return null == node ? stringValue : node.value;

    }

    final boolean method617(String str) {
        if (nodeTable == null) {
            return false;
        }

        if (nodeTable2 == null) {
            createSecondStringNodeTable();
        }

        for (StringNode sNode = (StringNode) nodeTable2.get(stringToLongKey(str)); sNode != null; sNode = (StringNode) nodeTable2.method1784()) {
            if (sNode.value.equals(str)) {
                return true;
            }
        }

        return false;

    }

    private static final long stringToLongKey(String s) {
        long var2 = 0L;
        for (int var4 = 0; var4 < s.length(); ++var4) {
            var2 = (long) (s.toCharArray()[var4] & 255) + (var2 << 5) - var2;
        }
        return var2;
    }

    private final void createSecondStringNodeTable() {
        nodeTable2 = new NodeTable(nodeTable.getSize());
        StringNode node = (StringNode) nodeTable.getFirst();

        while (node != null) {
            StringNode var3 = new StringNode(node.value);
            nodeTable2.put(var3, stringToLongKey(node.value));
            node = (StringNode) nodeTable.getNext();
        }
    }

    final int getIntValue(int key) {
        if (nodeTable == null) {
            return intValue;
        }
        IntegerNode iNode = (IntegerNode) nodeTable.get((long) key);
        return iNode != null ? iNode.value : intValue;
    }

    final boolean method621(int var2) {
        if (null != nodeTable) {
            if (nodeTable2 == null) {
                method622(109);
            }
            IntegerNode var3 = (IntegerNode) nodeTable2.get((long) var2);
            return var3 != null;
        } else {
            return false;
        }

    }

    private final void method622(int var1) {

        nodeTable2 = new NodeTable(nodeTable.getSize());
        for (IntegerNode var2 = (IntegerNode) nodeTable.getFirst(); null != var2; var2 = (IntegerNode) nodeTable.getNext()) {
            IntegerNode var4 = new IntegerNode((int) var2.uid);
            this.nodeTable2.put(var4, (long) var2.value);
        }

    }

    public ScriptConstantsTable() {
        stringValue = "null";
    }

    public final class NodeTable {

        Node[] headNodes;
        int capacity;
        private Node lastIterated;
        private Node lastRetrieved;
        private long lastKey;

        private int currentIndex = 0;

        public final Node getFirst() {
            currentIndex = 0;
            return getNext();
        }

        public final Node getNext() {
            Node var2;
            if (-1 > ~currentIndex && lastIterated != headNodes[currentIndex - 1]) {
                var2 = lastIterated;
                lastIterated = var2.next;
                return var2;
            } else {
                do {
                    if (~currentIndex <= ~capacity) {
                        return null;
                    }

                    var2 = headNodes[currentIndex++].next;
                } while (headNodes[currentIndex + -1] == var2);

                lastIterated = var2.next;
                return var2;
            }

        }

        final void put(Node node, long key) {
            if (null != node.prev) {
                node.unlink();
            }
            Node head = headNodes[(int) (key & (long) (capacity - 1))];
            node.next = head;
            node.uid = key;
            node.prev = head.prev;
            node.prev.next = node;
            node.next.prev = node;

        }

        NodeTable(int size) {
            headNodes = new Node[size];
            this.capacity = size;
            for (int i = 0; i < size; i++) {
                Node var3 = headNodes[i] = new Node();
                var3.prev = var3;
                var3.next = var3;
            }
        }

        final Node get(long key) {
            lastKey = key;
            Node head = headNodes[(int) (key & (long) (capacity - 1))];

            for (lastRetrieved = head.next; head != lastRetrieved; lastRetrieved = lastRetrieved.next) {
                if (key == lastRetrieved.uid) {
                    Node var5 = lastRetrieved;
                    lastRetrieved = lastRetrieved.next;
                    return var5;
                }
            }

            lastRetrieved = null;
            return null;
        }

        final Node method1784() {
            if (null == lastRetrieved) {
                return null;
            }
            Node head = headNodes[(int) (lastKey & (long) (capacity - 1))];

            while (head != lastRetrieved) {
                if (lastRetrieved.uid == lastKey) {
                    Node var3 = lastRetrieved;
                    lastRetrieved = lastRetrieved.next;
                    return var3;
                }

                lastRetrieved = lastRetrieved.next;
            }

            lastRetrieved = null;
            return null;

        }

        final int getSize() {
            return capacity;
        }

    }

    public class Node {

        public long uid;
        Node next;
        Node prev;

        final boolean isLinked() {
            return null != prev;
        }

        final void unlink() {
            if (null != prev) {
                prev.next = next;
                next.prev = prev;
                prev = null;
                next = null;
            }

        }

    }

    public final class IntegerNode extends Node {

        public int value;

        IntegerNode(int value) {
            this.value = value;
        }

    }

    public final class StringNode extends Node {

        public String value;

        StringNode(String str) {
            this.value = str;
        }

    }
}
