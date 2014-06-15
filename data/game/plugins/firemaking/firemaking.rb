require 'java'

java_import 'net.scapeemulator.game.task.Task'
java_import 'net.scapeemulator.game.model.mob.Animation'
java_import 'net.scapeemulator.game.model.mob.Direction'
java_import 'net.scapeemulator.game.model.object.ObjectOrientation'
java_import 'net.scapeemulator.game.model.object.ObjectType'
java_import 'net.scapeemulator.game.model.player.skills.Skill'
java_import 'net.scapeemulator.game.model.pathfinding.Path'
java_import 'net.scapeemulator.game.model.World'

module Firemaking

  FIREMAKING_ANIMATION = Animation.new 733
  TINDERBOX = 590
  ASHES = 592
  PREFERRED_DIRECTIONS = {Direction::WEST => {:x => -1, :y => 0}, Direction::EAST => {:x => 1, :y => 0}, Direction::SOUTH => {:x => 0, :y => -1}, Direction::NORTH => {:x => 0, :y => 1}}

  class << self
    def bind_handlers
      LOGS.each do |name, log|
        bind :item_on_item, :first => TINDERBOX, :second => log.item_id do
          player.start_action FiremakingAction.new(player, log, first, second)
        end
      end
    end
  end

  class FiremakingAction < Action
    def initialize(player, log, first, second)
      super(player, 2, true)
      @log = log
      @log_item = first.item.id != TINDERBOX ? first : second
    end

    def valid_position
      OBJECT_LIST.is_empty mob.position
    end

    def create_fire
      fire = OBJECT_LIST.put(mob.position, @log.fire_id, ObjectOrientation::WEST, ObjectType::PROP)
      TASK_SCHEDULER.schedule(RemoveFireTask.new fire) if !fire.nil?
      fire
    end

    def execute
      if !@cleared
        mob.get_walking_queue.reset
        @cleared = true
      end
      if mob.get_skill_set.get_current_level(Skill::FIREMAKING) < @log.lvl_req
        mob.send_message "You need a Firemaking level of #{@log.lvl_req} to burn these logs."
        stop
        return
      end
      if !valid_position
        mob.send_message 'You can\'t light a fire here.'
        stop
        return
      end
      if !@delay
        mob.get_inventory.remove(@log_item.item, @log_item.slot)
        mob.get_ground_items.add(@log.item_id, 1, mob.position)
        mob.play_animation FIREMAKING_ANIMATION
        diff = rand(mob.get_skill_set.get_current_level(Skill::FIREMAKING) - @log.lvl_req)
        diff = 15 if diff > 15
        @delay = rand(16 - diff) + 1
      end
      @delay -= 1
      if @delay == 0
        fire = create_fire
        mob.get_skill_set.add_experience(Skill::FIREMAKING, @log.xp)
        mob.cancel_animation
        mob.send_message 'The fire catches and the logs begin to burn.'
        mob.get_ground_items.remove(@log.item_id, mob.position)
        PREFERRED_DIRECTIONS.each do |direction, offsets|
          if mob.can_traverse direction
            @path = Path.new
            @path.add_first Position.new(mob.position.x + offsets[:x], mob.position.y + offsets[:y])
            mob.walk_path @path
            break
          end
        end
        mob.turn_to_position fire.position
        stop
      end
    end

  end

  class RemoveFireTask < Task
    def initialize(fire)
      super(rand(40) + 30, false)
      @fire = fire
    end

    def execute
      WORLD_ITEM_LIST.add(ASHES, 1, @fire.position)
      OBJECT_LIST.remove(@fire.position, @fire.type.get_object_group) if OBJECT_LIST.contains(@fire.id, @fire.position)
      stop
    end
  end

end

Firemaking::create_logs
Firemaking::bind_handlers