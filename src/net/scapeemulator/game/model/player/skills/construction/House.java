package net.scapeemulator.game.model.player.skills.construction;

import net.scapeemulator.game.model.Position;
import net.scapeemulator.game.model.World;
import net.scapeemulator.game.model.object.GroundObjectList;
import net.scapeemulator.game.model.object.GroundObjectList.GroundObject;
import net.scapeemulator.game.model.object.ObjectGroup;
import net.scapeemulator.game.model.player.Player;
import net.scapeemulator.game.model.player.PlayerVariables.Variable;
import net.scapeemulator.game.model.player.RegionPalette;
import net.scapeemulator.game.model.player.RegionPalette.Tile.Rotation;
import net.scapeemulator.game.model.player.SceneRebuiltListener;
import net.scapeemulator.game.model.player.skills.construction.hotspot.BuildableHotspot;
import net.scapeemulator.game.model.player.skills.construction.hotspot.Hotspot;
import net.scapeemulator.game.model.player.skills.construction.room.Room;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPlaced;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPosition;
import net.scapeemulator.game.model.player.skills.construction.room.RoomPreview;
import net.scapeemulator.game.model.player.skills.construction.room.RoomType;
import net.scapeemulator.game.msg.impl.MinimapUpdateMessage;
import net.scapeemulator.game.task.Action;
import net.scapeemulator.game.task.Task;

/**
 * Represents an instance of a Construction player owned house (POH).
 * 
 * @author David Insley
 */
public class House {

    /**
     * The size of the actual house region, fit inside of the main 13x13 region
     * palette. Should always be an odd number to guarantee a center subregion.
     */
    private static final int REGION_SIZE = 9;

    /**
     * Because the region palette is 13x13 but house regions can be smaller, we
     * need a subregion buffer around our house area.
     */
    private static final int PALETTE_OFFSET = (RegionPalette.PALETTE_SIZE - REGION_SIZE) / 2;

    public static final int HOUSE_X = 4000;
    public static final int HOUSE_Y = 4000;

    /**
     * X and Y coordinate of the bottom left corner of the house region.
     */
    public static final int BASE_X = HOUSE_X - ((REGION_SIZE / 2) * Room.ROOM_SIZE);
    public static final int BASE_Y = HOUSE_Y - ((REGION_SIZE / 2) * Room.ROOM_SIZE);

    /**
     * The owner of this house.
     */
    private final Player owner;

    /**
     * The list of all objects in this house.
     */
    private GroundObjectList objects;

    /**
     * A 3D array containing all rooms in the house region. Note that grass and
     * empty dungeon areas are not null but actually a type of room.
     */
    private final RoomPlaced[][][] rooms;

    /**
     * The style of the house, used when calculating palette locations for the
     * construct region packet.
     */
    private HouseStyle style;

    /**
     * The players selected portal in the game world used to access their POH.
     */
    private HousePortal worldPortal;

    /**
     * Set to true when the object list is populated with all of the house
     * hotspots.
     */
    private boolean populated = false;

    /**
     * Whether or not this house is locked to visitors.
     */
    private boolean locked = false;

    private BuildingSession buildSession;

    /**
     * Constructs a POH with the default settings. Portal location in
     * Rimmington, basic wood style, with a garden and parlour.
     */
    public House(Player owner) {
        this.owner = owner;
        worldPortal = HousePortal.RIMMINGTON;
        style = HouseStyle.FANCY_STONE;
        rooms = new RoomPlaced[4][REGION_SIZE][REGION_SIZE];
        for (int height = 0; height < 4; height++) {
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    rooms[height][x][y] = new RoomPlaced(this, new RoomPosition(height, x, y), Construction.defaultRoom(height), Rotation.NONE);
                }
            }
        }
        rooms[1][4][4] = new RoomPlaced(this, new RoomPosition(1, 4, 4), RoomType.GARDEN, Rotation.NONE);
        rooms[1][4][5] = new RoomPlaced(this, new RoomPosition(1, 4, 5), RoomType.PARLOUR, Rotation.NONE);
    }

    public void setRoom(int x, int y, int height, RoomType type, Rotation rotation) {
        rooms[height][x][y] = new RoomPlaced(this, new RoomPosition(height, x, y), type, rotation);
        populated = false;
    }
    
    /**
     * Resets the ground object list and populates it with the hotspots for each
     * room.
     */
    private void populateHouse() {
        objects = new GroundObjectList();
        for (int height = 0; height < 4; height++) {
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    rooms[height][x][y].createHotspots();
                }
            }
        }
        populated = true;
    }

    public void otherEnterPortal(Player player) {
        if (owner.getInHouse() != this) {
            player.sendMessage("They don't appear to be at home.");
            return;
        }
        if (locked) {
            player.sendMessage("You cannot access that house right now.");
            return;
        }
        if (buildSession != null) {
            player.sendMessage("That player is currently in building mode.");
            return;
        }
        sendHouse(player, getPortalPosition());
    }

    public void ownerEnterPortal(boolean building) {
        if (!populated) {
            populateHouse();
        }
        loadingInterface(owner, true);
        setBuildingMode(building);
        sendHouse(owner, getPortalPosition());
    }

    private void sendHouse(final Player player, final Position newPos) {
        Position pos = new Position(HOUSE_X, HOUSE_Y, getHeightOffset() + 1);
        player.teleport(pos);
        loadingInterface(player, true);
        if (!populated) {
            populateHouse();
        }
        RegionPalette palette = getRegionPalette();
        player.setConstructedRegion(palette);
        player.setSceneRebuiltListener(new SceneRebuiltListener() {
            @Override
            public void sceneRebuilt() {
                objects.fireAllEvents(player.getGroundObjectSynchronizer());
                objects.addListener(player.getGroundObjectSynchronizer());
                player.teleport(newPos);
                loadingInterface(player, true);
                player.startAction(new Action<Player>(player, 5, false) {
                    @Override
                    public void execute() {
                        stop();
                        loadingInterface(player, false);
                    }
                });
            }
        });
        player.setInHouse(this);
    }

    private Position getPortalPosition() {
        return new Position(HOUSE_X + 1, HOUSE_Y + 2, getHeightOffset() + 1);
    }

    public void clearRoomSpace(RoomPosition pos, boolean respawnObjects) {
        RoomType type = Construction.defaultRoom(pos.getHouseHeight());
        Position clearPos = style.getRoomPosition(type);
        for (int x = 0; x < Room.ROOM_SIZE; x++) {
            for (int y = 0; y < Room.ROOM_SIZE; y++) {
                for (ObjectGroup group : ObjectGroup.values()) {
                    Position objPos = new Position(pos.getBaseX() + x, pos.getBaseY() + y, getHeightOffset() + pos.getHouseHeight());
                    objects.remove(objPos, group);
                    if (respawnObjects && type != RoomType.NONE) {
                        GroundObject obj = World.getWorld().getGroundObjects().get(group, new Position(clearPos.getX() + x, clearPos.getY() + y, clearPos.getHeight()));
                        if (obj != null) {
                            objects.put(objPos, obj.getId(), obj.getRotation(), obj.getType());
                        }
                    }
                }
            }
        }

    }

    public void setBuildingMode(boolean buildingMode) {
        for (int height = 0; height < 4; height++) {
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    RoomPlaced room = rooms[height][x][y];
                    room.buildingMode(buildingMode);
                }
            }
        }
        buildSession = buildingMode ? new BuildingSession() : null;
    }

    /**
     * Gets the Room for the given coordinates in the POH. Note that the height
     * is for the actual position in the world (0-3 + ownerId * 4), IE the
     * players actual height, not the rooms height.
     * 
     * @param worldHeight the height of the position in the world
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the Room at the given coordinates if it exists, or null if it
     *         doesn't
     */
    public RoomPlaced forCoords(int worldHeight, int x, int y) {
        int roomX = (int) (4 + ((x - HOUSE_X) / 8.0));
        int roomY = (int) (4 + ((y - HOUSE_Y) / 8.0));
        try {
            return rooms[worldHeight - getHeightOffset()][roomX][roomY];
        } catch (IndexOutOfBoundsException e) {
            return null;
        }
    }

    /**
     * Constructs and returns the RegionPalette for this POH.
     * 
     * @return the constructed region palette
     */
    public RegionPalette getRegionPalette() {
        RegionPalette palette = new RegionPalette();
        for (int height = 0; height < rooms.length; height++) {
            for (int x = 0; x < REGION_SIZE; x++) {
                for (int y = 0; y < REGION_SIZE; y++) {
                    if (rooms[height][x][y].getType() != RoomType.NONE) {
                        palette.setTile(height, x + PALETTE_OFFSET, y + PALETTE_OFFSET, rooms[height][x][y].getPaletteSourceTile());
                    }
                }
            }
        }
        return palette;
    }

    public int getHeightOffset() {
        return owner.getId() * 4;
    }

    public GroundObjectList getObjectList() {
        return objects;
    }

    public HouseStyle getStyle() {
        return style;
    }
    
    public Player getOwner() {
        return owner;
    }

    public BuildingSession getBuildingSession() {
        return buildSession;
    }

    private static void loadingInterface(Player player, boolean show) {
        if (show) {
            player.send(MinimapUpdateMessage.HIDE_MINIMAP);
            player.getInterfaceSet().openOverlay(Construction.POH_LOADING_INTERFACE);
            player.setActionsBlocked(true);
        } else {
            player.send(MinimapUpdateMessage.RESET);
            player.getInterfaceSet().closeOverlay();
            player.setActionsBlocked(false);
        }
    }

    public class BuildingSession {

        private RoomPosition temp;
        private RoomPreview preview;
        private BuildableHotspot furnTemp;

        public void handleBuildOption(GroundObject object) {
            owner.turnToPosition(object.getCenterPosition());
            int x = object.getPosition().getX();
            int y = object.getPosition().getY();
            RoomPlaced room = forCoords(object.getPosition().getHeight(), x, y);
            if (room == null) {
                return;
            }
            Hotspot hotspot = room.getHotspot(x - room.getRoomPos().getBaseX(), y - room.getRoomPos().getBaseY(), object);
            if (hotspot instanceof BuildableHotspot) {
                ((BuildableHotspot) hotspot).handleBuildOption(this);
            }
        }

        public void handleFurnitureBuildInterface(int itemIndex) {
            if (furnTemp != null) {
                furnTemp.handleBuildInterface(this, itemIndex);
                furnTemp = null;
            }
        }

        public void delayReveal(BuildableHotspot hotspot) {
            World.getWorld().getTaskScheduler().schedule(new Task(2, false) {
                @Override
                public void execute() {
                    hotspot.buildingMode(true);
                }
            });
        }

        public void handleSelectRoomInterface(int childId) {
            owner.getInterfaceSet().closeWindow();
            RoomType roomType = RoomType.forInterfaceId(childId);
            if (temp == null || roomType == null) {
                return;
            }
            if(!roomType.validBuild(temp.getHouseHeight())) {
                owner.sendMessage("That room cannot be built on this floor.");
                temp = null;
                return;
            }
            (preview = new RoomPreview(House.this, roomType, temp)).previewRoom();
            temp = null;
            Construction.PREVIEW_DIALOGUE.displayTo(owner);

            /*
             * Start an action so that if the player moves or does anything else
             * the preview is cancelled, or after 3 minutes.
             */
            owner.startAction(new Action<Player>(owner, 500, false) {

                @Override
                public void execute() {
                }

                @Override
                public void stop() {
                    super.stop();
                    cancelPreview();
                }
            });

        }

        public void initRoomBuild(RoomPosition pos) {
            owner.getInterfaceSet().openWindow(Construction.ROOM_CREATE_INTERFACE);
            temp = pos;
        }

        public void rotatePreview(Rotation rot) {
            if (preview == null) {
                return;
            }
            preview.rotate(rot);
        }

        public void cancelPreview() {
            if (preview == null) {
                return;
            }
            clearRoomSpace(preview.getRoomPos(), true);
            preview = null;
        }

        public void finishPreview() {
            if (preview == null) {
                return;
            }
            RoomPosition pos = preview.getRoomPos();
            rooms[pos.getHouseHeight()][pos.getHouseX()][pos.getHouseY()] = new RoomPlaced(House.this, pos, preview.getType(), preview.getRoomRotation());
            clearRoomSpace(pos, false);
            rooms[pos.getHouseHeight()][pos.getHouseX()][pos.getHouseY()].createHotspots();
            setBuildingMode(true);
            preview = null;
            owner.stopAction();
            sendHouse(owner, owner.getPosition());
        }

        public void initFurnitureRemove(BuildableHotspot furnTemp) {
            this.furnTemp = furnTemp;
            if (owner.getVariables().getVar(Variable.CON_FURN_REMOVE) == 1) {
                finishFurnitureRemove(true);
            } else {
                Construction.FURNITURE_DELETION_DIALOGUE.displayTo(owner);
            }
        }

        public void finishFurnitureRemove(boolean confirm) {
            if (furnTemp == null) {
                return;
            }
            if (confirm) {
                furnTemp.finishRemove(this);
            }
            furnTemp = null;
        }

        public void initRoomDelete(RoomPosition pos) {
            if (pos.getHouseHeight() == 1) {
                if (rooms[2][pos.getHouseX()][pos.getHouseY()].getType() != RoomType.NONE) {
                    owner.sendMessage("You must remove the room above that one first.");
                    return;
                }
            }
            // TODO if deleting a dungeon stair, say do it from above
            Construction.ROOM_DELETION_DIALOGUE.displayTo(owner);
            temp = pos;
        }

        public void finishRoomDeletion(boolean confirm) {
            if (temp == null) {
                return;
            }
            if (confirm) {
                owner.getInterfaceSet().openOverlay(Construction.POH_LOADING_INTERFACE);
                clearRoomSpace(temp, true);
                rooms[temp.getHouseHeight()][temp.getHouseX()][temp.getHouseY()] = new RoomPlaced(House.this, temp, Construction.defaultRoom(temp.getHouseHeight()), Rotation.NONE);
                populateHouse();
                setBuildingMode(true);
                sendHouse(owner, owner.getPosition());
            }
            temp = null;
        }

        public Player getBuilder() {
            return owner;
        }

        public void setFurniturePlaceholder(BuildableHotspot temp) {
            furnTemp = temp;
        }
    }

}
