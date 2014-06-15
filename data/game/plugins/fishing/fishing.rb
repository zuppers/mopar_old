require 'java'

java_import 'net.scapeemulator.game.task.DistancedAction'
java_import 'net.scapeemulator.game.model.mob.Animation'

module Fishing
  class << self
    def bind_handlers
      bind :npc, :option => :one do
        SPOTS.each do |name, spot|
          if(npc.type == spot.npc_id)
            player.start_action FishingAction.new(player, npc, spot, 1)
            ctx.stop
          end
        end
      end
      bind :npc, :option => :two do
        SPOTS.each do |name, spot|
          if(npc.type == spot.npc_id)
            player.start_action FishingAction.new(player, npc, spot, 2)
            ctx.stop
          end
        end

      end
    end
  end

  class FishingAction < DistancedAction
    def initialize(player, npc, spot, option)
      super(4, true, player, npc.position, 1)
      @npc = npc
      @spot = spot
      @option = option
      @tool = @spot.tools[@option-1]
    end

    def executeAction

      if !mob.get_walking_queue.is_empty
        return
      end

      if !@turned
        mob.turn_to_position(@npc.position)
        @turned = true
      end
      if !@spot.check_reqs(mob, @option)
        mob.cancel_animation
        stop
        return
      end
      if !@started
        mob.send_message @tool.message
        @started = true
      end
      mob.play_animation(Animation.new @tool.animation_id)
      @spot.attempt_catch(mob, @option)
    end

  end

end

Fishing::create_tools
Fishing::create_fish_defs
Fishing::create_spots
Fishing::bind_handlers