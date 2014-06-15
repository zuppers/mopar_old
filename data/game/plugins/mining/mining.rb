=begin
* Name: mining.rb
* A mining plugin for the scapeemulator (or rune-emu) source
* Author: Davidi2 (David Insley)
* Date: September 26, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.model.object.GroundObjectListenerAdapter'
java_import 'net.scapeemulator.game.task.Task'
java_import 'net.scapeemulator.game.task.DistancedAction'
java_import 'net.scapeemulator.game.model.player.Equipment'
java_import 'net.scapeemulator.game.model.mob.Animation'

# A <position, rock> map of every mine-able rock in the game
ROCKS = {}

#These are rocks that are in the world already depleted, rather than have them go unhandled this way they will say they have been depleted.
DEPLETED_ROCKS = [ 450, 452 ]

#The <rock_id, depleted_rock_id> map
DEPLETED_IDS = {}

module Mining
  class << self
    def bind_handlers
      bind :obj do
        if option.eql?("mine")
          if ROCKS.include?(obj.position)
            player.start_action(MiningAction.new(player, ROCKS[obj.position]))
            ctx.stop
          end
        end
      end
    end

    def refresh
      listener = RockObjectListener.new
      OBJECT_LIST.fire_all_events(listener)
      OBJECT_LIST.add_listener(listener)
    end

  end

  class Rock
    attr_reader :type, :object, :depleted
    def initialize(object, type, depleted=false)
      @object      = object
      @original_id = object.id
      @type        = type
      @depleted    = depleted
      if DEPLETED_IDS.key?(@original_id)
        @depleted_id = DEPLETED_IDS[@original_id]
      else
        DEPLETED_IDS[@original_id] = find_depleted_id
        @depleted_id = DEPLETED_IDS[@original_id]
      end
    end

    #mine the ore, turn into depleted rock
    def mine
      @object.id = @depleted_id
      TASK_SCHEDULER.schedule(RespawnTask.new(self))
      @depleted = true
    end

    #respawn ore, called by the respawn task
    def respawn
      @object.id = @original_id
      @depleted = false
    end

    def find_depleted_id
      case @original_id
      when 2090..2109
        return (@original_id % 2 == 0) ? 450 : 452
      when 31062..31088
        return 31059 + (@original_id % 3)
      when 11945..11962
        case @original_id % 3
        when 0
          return 11556
        when 1
          return 11557
        when 2
          return 11555
        end
      else
        return 450
      end
    end
  end

  class MiningAction < DistancedAction
    def initialize(player, rock)
      super(1, true, player, rock.object.position, 1)
      @rock = rock
    end

    def get_mining_lvl
      mob.get_skill_set.get_current_level Skill::MINING
    end

    #use either the currently equipped pickaxe if we have the requirement, or the best in the inventory.
    def find_pickaxe
      weapon = mob.get_equipment.get Equipment::WEAPON
      if !weapon.nil?
        PICKAXES.each do |name, pickaxe|
          return pickaxe if (weapon.id == pickaxe.item_id && get_mining_lvl >= pickaxe.lvl_req)
        end
      end
      best_pickaxe = nil
      PICKAXES.each do |name, pickaxe|
        next if(!best_pickaxe.nil? && pickaxe.speed < best_pickaxe.speed)
        (best_pickaxe = pickaxe) if (mob.get_inventory.slot_of(pickaxe.item_id) > -1 && get_mining_lvl >= pickaxe.lvl_req)
      end
      best_pickaxe
    end

    def should_mine
      #calculations and shit here TODO
      return true if rand(5) > 2
      false
    end

    def executeAction
      if @rock.depleted
        mob.send_message 'There is currently no ore available in this rock.'
        stop
      return
      end

      if !mob.not_walking || !mob.get_walking_queue.is_empty
      return
      end

      if !@turned
        mob.turn_to_position(@rock.object.position)
        @turned = true
      end

      if get_mining_lvl < @rock.type.lvl_req
        mob.send_message "You need a Mining level of #{@rock.type.lvl_req} to mine this ore."
        stop
      return
      end

      pickaxe = find_pickaxe
      if pickaxe.nil?
        mob.send_message 'You do not have a pickaxe that you have the Mining level to use.'
        stop
      return
      end

      if mob.get_inventory.free_slots < 1
        mob.send_message 'Your inventory is too full to hold any more ore.'
        stop
      return
      end

      if !@started
        mob.send_message 'You swing your pickaxe at the rock.'
        mob.play_animation pickaxe.animation
        @started = true
      return
      end

      mob.play_animation pickaxe.animation
      if should_mine
        item = Item.new @rock.type.item_id
        mob.get_inventory.add item
        mob.send_message "You manage to mine some #{item.definition.name.downcase}."
        mob.get_skill_set.add_experience(Skill::MINING, @rock.type.xp)
        @rock.mine
        mob.cancel_animation
        stop
      end
    end

  end

  class RespawnTask < Task
    def initialize(rock)
      super(rock.type.respawn_ticks, false)
      @rock = rock
    end

    def execute
      @rock.respawn
      stop
    end
  end

  class RockObjectListener < GroundObjectListenerAdapter
    def groundObjectAdded(object)
      if DEPLETED_ROCKS.include? object.id
        ROCKS[object.position] = Rock.new(object, nil, true)
      return
      end

      ORES.each do |name, ore|
        if ore.object_ids.include?object.id
          ROCKS[object.position] = Rock.new(object, ore)
        return
        end
      end
    end
  end

end

Mining::create_ores
Mining::create_pickaxes
Mining::bind_handlers
Mining::refresh