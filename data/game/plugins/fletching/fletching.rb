=begin
* Name: fletching.rb
* The fletching skill handler
* Author: Davidi2 (David Insley)
* Date: October 26, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.model.player.interfaces.ComponentListenerAdapter'
java_import 'net.scapeemulator.game.msg.impl.ScriptMessage'
java_import 'net.scapeemulator.game.model.player.ScriptInputListener'

module Fletching

  SPACER = '<br><br><br><br><br>'
  class << self
    def bind_handlers
      RECIPES.each do |hash, recipes|
        bind :item_on_item, :first => ItemOnItemDispatcher::reverse_hash(hash)[0], :second => ItemOnItemDispatcher::reverse_hash(hash)[1] do
          Fletching::show_fletching_interface(player, recipes)
        end
      end
    end

    def show_fletching_interface(player, recipes)
      id = recipes.size == 1 ? 309 : (301 + recipes.size)
      for i in (0...recipes.size)
        player.send InterfaceItemMessage.new(id, i + 2, 200, recipes.values[i].product_id)
        string_index = 7 + (4 * i) + recipes.size - 2
        item_name = Item.new(recipes.values[i].product_id).definition.name
        player.set_interface_text(id, string_index, "<br><br><br><br><br>#{item_name}");
      end
      player.get_interface_set.open_chatbox id
      listener = FletchingInterfaceListener.new
      player.get_interface_set.get_chatbox.set_listener(listener)
      listener.set_context(player, recipes)
    end
  end

  class FletchingTask < Action
    def initialize(player, recipe, amt)
      super(player, recipe.delay, true)
      @recipe = recipe
      @amt = amt
      @count = 0
    end

    def execute
      if(!@cleared)
        mob.get_walking_queue.reset
        @cleared = true
      end
      if @recipe.check_reqs(mob)
        @recipe.fletch(mob)
        @count += 1
        if @count >= @amt
          mob.cancel_animation
          stop
        end
      else
        mob.cancel_animation
        stop
      end
    end

  end

  class FletchingInterfaceListener < ComponentListenerAdapter
    def set_context(player, recipes)
      @player = player
      @recipes = recipes
    end

    def inputPressed(component, component_id, dynamic_id)
      parent_id = component.get_current_id
      offset = parent_id == 309 ? 3 : (4 + (parent_id - 303))
      component_id -= offset
      recipe = @recipes.values[component_id / 4]
      option = 3 - (component_id % 4)
      case option
      when 0..1
        amt = 5 ** option
      when 2
        amt = parent_id == 309 ? 'X' : 10
      when 3
        amt = parent_id == 309 ? recipe.calculate_max(@player) : 'X'
      end
      if amt == 'X'
        close
        input_listener = FletchingInputListener.new
        input_listener.set_context(@player, recipe)
        @player.get_script_input.show_integer_script_input input_listener
      else
        @player.get_interface_set.get_chatbox.reset
        @player.start_action FletchingTask.new(@player, recipe, amt)
      end
    end

    def componentClosed(unused)
      component = player.get_interface_set.get_chatbox
      component.remove_listener
      component.reset
    end

    def close
      component = @player.get_interface_set.get_chatbox
      component.remove_listener
      component.reset
    end
  end

  class FletchingInputListener < ScriptInputListener
    def set_context(player, recipe)
      @player = player
      @recipe = recipe
    end

    def intInputReceived(value)
      @player.start_action FletchingTask.new(@player, @recipe, value) if value > 0
      @player.get_script_input.reset
    end
  end
end

Fletching::create_recipes
Fletching::bind_handlers