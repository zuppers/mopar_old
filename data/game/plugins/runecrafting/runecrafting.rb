=begin
* Name: runecrafting.rb
* Runecrafting skill handler script
* Author: Davidi2 (David Insley)
* Date: October 14, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.task.Task'
java_import 'net.scapeemulator.game.task.DistancedAction'
java_import 'net.scapeemulator.game.model.mob.Animation'
java_import 'net.scapeemulator.game.model.SpotAnimation'
java_import 'net.scapeemulator.game.model.World'
java_import 'net.scapeemulator.game.model.player.skills.Skill'
java_import 'net.scapeemulator.game.model.player.Item'

module Runecrafting

  PURE_ESSENCE = 7936
  RUNE_ESSENCE = 1436
  CRAFT_ANIMATION = Animation.new 791
  CRAFT_GRAPHIC = SpotAnimation.new(186, 0, 100)

  ALTARS = {:air => 2478, :mind => 2479, :water => 2480, :earth => 2481, :fire => 2482, :body => 2483,
    :cosmic => 2484, :chaos => 2487, :astral => 17010, :nature => 2486, :law => 2485, :death => 2488,
    :blood => 30624
  }

  TIARAS = {:air => 5527, :mind => 5529, :water => 5531, :earth => 5535, :fire => 5537, :body => 5533,
    :cosmic => 5539, :chaos => 5543, :nature => 5541, :law => 5545, :death => 5547, :blood => 5549
  }

  class << self
    def bind_handlers
      bind :obj do
        if option.eql?("craft-rune")
          ALTARS.each do |name, id|
            if id == obj.id
              player.start_action(RunecraftingAction.new(player, obj, RUNE_TYPES[name]))
              ctx.stop
            end
          end
        end
      end
    end

  end

  class RunecraftingAction < DistancedAction
    def initialize(player, object, rune_type)
      super(1, true, player, object.position, 3)
      @object = object
      @rune_type = rune_type
    end

    def get_rc_lvl
      return mob.get_skill_set.get_current_level(Skill::RUNECRAFTING)
    end

    def executeAction

      if !mob.not_walking
      return
      end

      mob.turn_to_position(@object.get_center_position)

      multiplier = @rune_type.get_rune_multiplier(get_rc_lvl)
      if multiplier < 1
        mob.send_message("You need a Runecrafting level of #{@rune_type.lvl_reqs[0]} to do that.")
        stop
      return
      end

      #Prioritize rune ess over pure if we don't need it
      removed = mob.get_inventory.remove(Item.new(PURE_ESSENCE, 28)) if @rune_type.pure || !mob.get_inventory.contains(RUNE_ESSENCE)
      removed = mob.get_inventory.remove(Item.new(RUNE_ESSENCE, 28)) if removed.nil? && !@rune_type.pure

      if(removed.nil?)
        mob.send_message("You don't have enough essence to craft any runes.")
        stop
      return
      end

      amount = removed.amount
      runes = Item.new(@rune_type.item_id, amount * multiplier)
      mob.send_message("You make some #{runes.definition.name.downcase}s.")
      mob.get_skill_set.add_experience(Skill::RUNECRAFTING, @rune_type.xp * amount)
      mob.get_inventory.add(runes)
      mob.play_animation(CRAFT_ANIMATION)
      mob.play_spot_animation(CRAFT_GRAPHIC)
      stop
    end

  end
end

Runecrafting::create_rune_types
Runecrafting::bind_handlers