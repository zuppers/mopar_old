require 'java'

java_import 'net.scapeemulator.game.model.player.appearance.Appearance'
java_import 'net.scapeemulator.game.GameServer'
java_import 'net.scapeemulator.game.model.World'
java_import 'net.scapeemulator.game.model.object.ObjectType'
java_import 'net.scapeemulator.game.model.mob.Animation'
java_import 'net.scapeemulator.game.model.player.Item'
java_import 'net.scapeemulator.game.model.player.skills.SkillSet'
java_import 'net.scapeemulator.game.model.player.skills.construction.Construction'
java_import 'net.scapeemulator.game.model.object.ObjectOrientation'
java_import 'net.scapeemulator.game.model.SpotAnimation'
java_import 'net.scapeemulator.game.msg.impl.inter.InterfaceItemsMessage'
java_import 'net.scapeemulator.game.msg.impl.inter.InterfaceItemMessage'
java_import 'net.scapeemulator.game.msg.impl.inter.InterfaceVisibleMessage'
java_import 'net.scapeemulator.game.msg.impl.inter.InterfaceOpenMessage'
java_import 'net.scapeemulator.game.msg.impl.ConfigMessage'
java_import 'net.scapeemulator.game.msg.impl.camera.CameraMoveMessage'
java_import 'net.scapeemulator.game.msg.impl.camera.CameraResetMessage'
java_import 'net.scapeemulator.game.msg.impl.camera.CameraAngleMessage'
java_import 'net.scapeemulator.game.msg.impl.camera.CameraFaceMessage'
java_import 'net.scapeemulator.game.model.player.RegionPalette'
java_import 'net.scapeemulator.game.msg.impl.RegionConstructMessage'
java_import 'net.scapeemulator.game.model.player.minigame.stealingcreation.StealingCreation'

# Common administrator commands
bind :cmd, :name => 'looks' do
  player.get_appearance.show_appearance_interface
end

bind :cmd, :name => 'sc' do
  h = player.get_position.height
  h = args[2].to_i if args.length > 2
  player.teleport(Position.new(args[0].to_i, args[1].to_i, h))
  player.set_constructed_region StealingCreation::sc.get_region_palette
end

bind :cmd, :name => 'con' do
  Construction::ENTER_PORTAL_DIALOGUE.display_to player
end

bind :cmd, :name => 'build' do
  player.get_house.building_mode(args[0].to_i == 1)
end

bind :cmd, :name => 'chat' do
  player.set_forced_chat '~HELLO how r u'
end

bind :cmd, :name => 'window' do
  player.interface_set.open_window(args[0].to_i)
end

# TODO start remove
bind :cmd, :name => 'min' do
  player.set_min
end

bind :cmd, :name => 'noclip' do
  player.toggle_clipping
end

bind :cmd, :name => 'max' do
  player.set_max
end

bind :cmd, :name => 'spawn' do
  player.set_spawn_pos args[0].to_i
end
# TODO end remove

bind :cmd, :name => 'overlay' do
  player.interface_set.open_overlay(args[0].to_i)
end

bind :cmd, :name => 'item' do
  amount = 1
  amount = args[1].to_i if args.length > 1
  player.get_inventory.add Item.new(args[0].to_i, amount)
end

bind :cmd, :name => 'chatbox' do
  player.interface_set.open_chatbox(args[0].to_i)
end

bind :cmd, :name => 'inventory' do
  player.interface_set.open_inventory(args[0].to_i)
end

bind :cmd, :name => 'text' do
  player.set_interface_text(args[0].to_i, args[1].to_i, args[2])
end

bind :cmd, :name => 'bitstate' do
  player.state_set.set_bit_state(args[0].to_i, args[1].to_i)
end

bind :cmd, :name => 'state' do
  player.state_set.set_state(args[0].to_i, args[1].to_i)
end

bind :cmd, :name => 'config' do
  player.send ConfigMessage.new(args[0].to_i, args[1].to_i);
end

bind :cmd, :name => 'cam' do
  player.send CameraMoveMessage.new(args[0].to_i, args[1].to_i, args[2].to_i, args[3].to_i, args[4].to_i)
end

bind :cmd, :name => 'resetcam' do
  player.send CameraResetMessage.new
end

bind :cmd, :name => 'camface' do
  player.send CameraFaceMessage.new(args[0].to_i, args[1].to_i, args[2].to_i, args[3].to_i, args[4].to_i)
end

bind :cmd, :name => 'dumpcon' do
  player.get_house.dump_room_data
end

bind :cmd, :name => 'camangle' do
  player.send CameraAngleMessage.new(args[0].to_i, args[1].to_i)
end

bind :cmd, :name => 'visible' do
  player.send InterfaceVisibleMessage.new(args[0].to_i, args[1].to_i, (args[2].to_i == 1))
end

bind :cmd, :name => 'object' do
  rot = ObjectOrientation::WEST
  rot = args[1].to_i if args.length > 1
  type = ObjectType::PROP
  type = ObjectType::for_id(args[2].to_i) if args.length > 2
  OBJECT_LIST.put(player.position, args[0].to_i, rot, type)
end

bind :cmd, :name => 'pnpc' do
  player.set_pnpc args[0].to_i
end

bind :cmd, :name => 'anim' do
  player.play_animation(Animation.new(args[0].to_i))
end

bind :cmd, :name => 'gfx' do
  d = args.length > 1 ? args[1].to_i : 0
  h = args.length > 2 ? args[2].to_i : 0
  player.play_spot_animation(SpotAnimation.new(args[0].to_i, d, h))
end

bind :cmd, :name => 'proj' do
  player.get_combat_handler.send_projectile(args[0].to_i, args[1].to_i, args[2].to_i, args[3].to_i, args[4].to_i)
end

bind :cmd, :name => 'master' do
  skills = player.get_skill_set
  for id in 0...Skill::AMOUNT_SKILLS
    skills.add_experience(id, SkillSet::MAXIMUM_EXPERIENCE)
  end
end

bind :cmd, :name => 'xp' do
  player.get_skill_set.add_experience(args[0].to_i, args[1].to_i)
end

bind :cmd, :name => 'empty' do
  player.get_inventory.empty
end

bind :cmd, :name => 'tele' do
  h = player.get_position.height
  h = args[2].to_i if args.length > 2
  player.teleport(Position.new(args[0].to_i, args[1].to_i, h));
end

bind :cmd, :name => 'teleto' do
  target = World::get_world.get_player_by_name args[0].sub('_', ' ')
  if !target.nil?
    player.teleport(target.get_position)
  else
    player.send_message 'No player found by that name. Remember to use underscores instead of spaces!'
  end
end

bind :cmd, :name => 'teletome' do
  target = World::get_world.get_player_by_name args[0].sub('_', ' ')
  if !target.nil?
    target.teleport(player.get_position)
  else
    player.send_message 'No player found by that name. Remember to use underscores instead of spaces!'
  end
end

bind :cmd, :name => 'npc' do
  create_npc :normal, :id => args[0].to_i, :position => player.get_position do |npc| npc.turn_to_target player end
end

bind :cmd, :name => 'pos' do
  player.send_message(player.position.to_string);
end

bind :cmd, :name => 'ge' do
  player.get_grand_exchange_handler.show_interface
end

bind :cmd, :name => 'shop' do
  player.get_shop_handler.open_shop 'Lumbridge General Store'
end

bind :cmd, :name => 'bank' do
  player.start_bank_session
end

bind :cmd, :name => 'clear' do
  player.get_bank_session.clear_search
end

bind :cmd, :name => 'shopid' do
  player.get_shop_handler.shop_script args[0].to_i
end
