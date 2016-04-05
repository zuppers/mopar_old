require 'java'

java_import 'net.scapeemulator.game.model.player.action.ReachDistancedAction'

SHOPKEEPERS = {
  516 => 'Obli\'s General Store',
  517 => 'Fernahei\'s Fishing Hut',
  519 => 'Bob\'s Brilliant Axes',
  520 => 'Falador General Store', 521 => 'Falador General Store',
  522 => 'Varrock General Store', 523 => 'Varrock General Store',
  524 => 'Al Kharid General Store', 525 => 'Al Kharid General Store',
  526 => 'Lumbridge General Store', 527 => 'Lumbridge General Store',
  530 => 'Rimmington General Store', 531 => 'Rimmington General Store',
  534 => 'Zanaris General Store', 535 => 'Zanaris General Store',
  538 => 'Helmet Shop',
  540 => 'Gem Trader', # TODO
  541 => 'Zeke\'s Superior Scimitars',
  542 => 'Louie\'s Armoured Legs Bazaar',
  544 => 'Ranael\'s Super Skirt Store',
  545 => 'Dommik\'s Crafting Store',
  546 => 'Zaff\'s Superior Staves',
  548 => 'Thessalia\'s Fine Clothes',
  549 => 'Horvik\'s Armour Shop',
  550 => 'Lowe\'s Archery Emporium',
  551 => 'Varrock Sword Shop', 552 => 'Varrock Sword Shop',
  553 => 'Aubury\'s Rune Shop',
  554 => 'Fancy Clothes Store',
  556 => 'Grum\'s Gold Exchange',
  557 => 'Food Store',
  558 => 'Gerrant\'s Fishy Business', # TODO
  559 => 'Brian\'s Battleaxe Bazaar',
  560 => 'Jiminua\'s Jungle Store',
  566 => 'Irksol',
  573 => 'Ardougne Fur Stall',
  577 => 'Cassie\'s Shield Shop',
  579 => 'Drogo\'s Mining Emporium',
  580 => 'Flynn\'s Mace Market',
  581 => 'Wayne\'s Chains',
  582 => 'Dwarven Shopping Store', # TODO
  583 => 'Betty\'s Magic Emporium',
  584 => 'Herquin\'s Gems',
  585 => 'Rommik\'s Crafty Supplies',
  586 => 'Gaius\' Two Handed Shop',
  590 => 'Aemad\'s Adventuring Supplies',
  594 => 'Nurmof\'s Pickaxe Shop',
  595 => 'Ye Olde Tea Shoppe',
  1316 => 'Fremennik Fur Trader',
  1860 => 'Brian\'s Archery Supplies',
  2233 => 'Draynor Seed Market',
  3671 => 'Wine Shop',
  4294 => 'Warriors Guild Potion Shop', # Lilly
  4295 => 'Warriors Guild Armoury', # Anton
  4558 => 'Crossbow Shop', # Hirko
  4559 => 'Crossbow Shop', # Holoy
  4563 => 'Crossbow Shop', # Hura
  5109 => 'Nardah Hunter Shop', # Artimeus
  5110 => 'Aleck\'s Hunter Emporium',
  5780 => 'Reldak\'s Leather Armour',
  5781 => 'Miltog\'s Lamps'
}

bind :npc, :option => :three do
  if(SHOPKEEPERS.has_key?npc.type)
    player.start_action OpenShopAction.new(player, npc)
    ctx.stop
  end
end

class OpenShopAction < ReachDistancedAction
  def initialize(player, npc)
    super(0, true, player, npc.position, 1)
    @npc = npc
  end

  def executeAction
    mob.get_shop_handler.open_shop SHOPKEEPERS[@npc.type]
    stop
  end
end
