require 'java'

# TODO change to map
bind :npc, :option => :two do
  if(npc.type == 519)
    player.get_shop_handler.open_shop 'Bob\'s Brilliant Axes'
    ctx.stop
  elsif(npc.type == 520 || npc.type == 521)
    player.get_shop_handler.open_shop 'Falador General Store'
    ctx.stop
  elsif(npc.type == 522 || npc.type == 523)
    player.get_shop_handler.open_shop 'Varrock General Store'
    ctx.stop
  elsif(npc.type == 524 || npc.type == 525)
    #player.get_shop_handler.open_shop 'Al Kharid General Store'
    ctx.stop
  elsif(npc.type == 526 || npc.type == 527)
    player.get_shop_handler.open_shop 'Lumbridge General Store'
    ctx.stop
  elsif(npc.type == 538)
    player.get_shop_handler.open_shop 'Helmet Shop'
    ctx.stop
  elsif(npc.type == 546)
    player.get_shop_handler.open_shop 'Zaff\'s Superior Staves'
    ctx.stop
  elsif(npc.type == 548)
    player.send_message 'Trying to find the shop ID for this' 
    #player.get_shop_handler.open_shop 'Thessalia\'s Fine Clothes' TODO
    ctx.stop
  elsif(npc.type == 549)
    player.get_shop_handler.open_shop 'Horvik\'s Armour Shop'
    ctx.stop
  elsif(npc.type == 550)
    player.get_shop_handler.open_shop 'Lowe\'s Archery Emporium'
    ctx.stop
  elsif(npc.type == 551 || npc.type == 552)
    player.get_shop_handler.open_shop 'Varrock Sword Shop'
    ctx.stop
  elsif(npc.type == 553 || npc.type == 552)
    player.get_shop_handler.open_shop 'Aubury\'s Rune Shop'
    ctx.stop
  end
end
