require 'java'

SHOPKEEPERS = {
  519 => 'Bob\'s Brilliant Axes',
  520 => 'Falador General Store', 521 => 'Falador General Store',
  522 => 'Varrock General Store', 523 => 'Varrock General Store',
  524 => 'Al Kharid General Store', 525 => 'Al Kharid General Store',
  526 => 'Lumbridge General Store', 527 => 'Lumbridge General Store',
  538 => 'Helmet Shop',
  546 => 'Zaff\'s Superior Staves',
  #548 => 'Thessalia\'s Fine Clothes', TODO FIND ID
  549 => 'Horvik\'s Armour Shop',
  550 => 'Lowe\'s Archery Emporium',
  551 => 'Varrock Sword Shop', 552 => 'Varrock Sword Shop',
  553 => 'Aubury\'s Rune Shop',
  2233 => 'Draynor Seed Market',
  3671 => 'Wine Shop'
}

bind :npc, :option => :three do
  if(SHOPKEEPERS.has_key?npc.type)
    player.start_action OpenShopAction.new(player, npc)
    ctx.stop
  end
end

class OpenShopAction < DistancedAction
  def initialize(player, npc)
    super(0, true, player, npc.position, 1)
    @npc = npc
  end

  def executeAction
    mob.get_shop_handler.open_shop SHOPKEEPERS[@npc.type]
    stop
  end
end