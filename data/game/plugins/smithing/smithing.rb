=begin
* Name: smithing.rb
* A smithing plugin
* Author: Davidi2 (David Insley)
* Date: November 11, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.task.Action'
java_import 'net.scapeemulator.game.model.mob.Animation'
java_import 'net.scapeemulator.game.model.SpotAnimation'
java_import 'net.scapeemulator.game.task.DistancedAction'
java_import 'net.scapeemulator.game.model.player.ComponentListenerAdapter'
java_import 'net.scapeemulator.game.msg.impl.ScriptMessage'
java_import 'net.scapeemulator.game.model.player.IntegerScriptInputListener'

module Smithing

  HAMMER = 2347
  SMITHING_WINDOW = 300
  SMELTING_CHATBOX = 311
  
  SMITHING_TABLES = {}
  SMITHING_ANIMATION = Animation.new 898
  SMELTING_ANIMATION = Animation.new 899
  
  ANVILS    = [2783]
  FURNACES  = [11666, 36956]
  SMITHING_AMOUNTS = ['ALL', 'X', 5, 1]
  SMELTING_AMOUNTS = ['X', 10, 5, 1]
  
  GREEN_TEXT = '<col=00FF00>'
  WHITE_TEXT = '<col=FFFFFF>'
  BLACK_TEXT = '<col=000000>'
  SPACER     = '<br><br><br><br><br>'
  
  class << self
    
    def bind_handlers
      
      BARS.each do |name, bar|
        ANVILS.each do |obj_id|
          bind :item_on_obj, :item_id => bar.bar_id, :obj_id => obj_id do
            player.start_action AnvilAction.new(player, obj, bar)
          end
        end
      end
      
      TYPES.each do |name, type|
        SMITHING_AMOUNTS.each_index do |i|
          bind :btn, :id => SMITHING_WINDOW, :component => (3 + i + type.interface_offset) do
            if player.get_action.instance_of? AnvilAction
              player.get_action.start_smithing(type, SMITHING_AMOUNTS[i])
            else
              p 'Hmm...thats not right... someone could be trying to packet edit.'
            end
          end
        end
      end
      
      BARS.values.each_index do |bar_i|
        SMELTING_AMOUNTS.each_index do |amt_i|
          bind :btn, :id => SMELTING_CHATBOX, :component => (13 + amt_i + bar_i * 4) do
            if player.get_action.instance_of? FurnaceAction
              player.get_interface_set.close_chatbox
              player.get_action.start_smelting(BARS.values[bar_i], SMELTING_AMOUNTS[amt_i])
            else
              p 'Hmm...thats not right... someone could be trying to packet edit.'
            end
          end
        end
      end
      
      bind :obj, :option => :two do
        if FURNACES.include?(obj.id)
          player.start_action FurnaceAction.new(player, obj)
        end
      end
      
    end    
  end

  class AnvilAction < DistancedAction
    def initialize(player, anvil, bar)
      super(1, true, player, anvil.position, 1)
      @anvil = anvil
      @bar = bar
    end

    def executeAction
      if !mob.get_walking_queue.is_empty
      return
      end
      if !@interface_shown
        mob.turn_to_position @anvil.position
        if mob.get_inventory.contains HAMMER
          show_smithing_interface
          @interface_shown = true
        else
          mob.send_message 'You need a hammer to make anything.'
          stop
        end
      end
    end
    
    def show_smithing_interface
      SMITHING_TABLES[@bar].each do |product|
        mob.send InterfaceItemMessage.new(SMITHING_WINDOW, product.type.interface_offset, product.type.amount, product.item_id)
        color = WHITE_TEXT if mob.get_skill_set.get_current_level(Skill::SMITHING) >= product.lvl_req
        mob.set_interface_text(SMITHING_WINDOW, product.type.interface_offset + 1, "#{color}#{product.type.name}");
        color = ''
        color = GREEN_TEXT if mob.get_inventory.get_amount(@bar.bar_id) >= product.type.bars
        mob.set_interface_text(SMITHING_WINDOW, product.type.interface_offset + 2, "#{color}#{product.type.bars} Bars");
      end
      mob.interface_set.open_window SMITHING_WINDOW
    end
    
    def start_smithing(type, amt)
      product = get_product type
      case amt
      when 'X'
        input_listener = SmithingInputListener.new
        input_listener.set_context(mob, product)
        mob.get_script_input.set_integer_input_listener input_listener
        mob.get_script_input.show_integer_script_input
        return
      when 'ALL'
        amt = mob.get_inventory.get_amount(@bar.bar_id) / type.bars
      end
      if product.check_reqs(mob, true)
        mob.start_action SmithingTask.new(4, mob, product, amt)
      end
    end

    def get_product(type)
      SMITHING_TABLES[@bar].each do |product|
        return product if product.type == type
      end
      nil
    end

  end
  
  class FurnaceAction < DistancedAction
    def initialize(player, furnace)
      super(1, true, player, furnace.position, 3)
      @furnace = furnace
    end

    def executeAction
      if !mob.get_walking_queue.is_empty
        return
      end
      if !@interface_shown
        mob.turn_to_position @furnace.get_center_position
        show_smelting_chatbox
        @interface_shown = true
      end
    end
    
    def show_smelting_chatbox
      iim_offset = 4
      bar_name_offset = 16

      BARS.each do |name, bar|
        mob.send InterfaceItemMessage.new(SMELTING_CHATBOX, iim_offset, 140, bar.bar_id)
        color = BLACK_TEXT if mob.get_skill_set.get_current_level(Skill::SMITHING) >= bar.smelt_lvl_req
        mob.set_interface_text(SMELTING_CHATBOX, bar_name_offset, "#{SPACER}#{color}#{name.to_s.capitalize}");
        bar_name_offset += 4
        iim_offset += 1
      end
      mob.interface_set.open_chatbox SMELTING_CHATBOX
    end
    
    def start_smelting(bar, amt)
      if amt == 'X'
        input_listener = SmithingInputListener.new
        input_listener.set_context(mob, bar)
        mob.get_script_input.set_integer_input_listener input_listener
        mob.get_script_input.show_integer_script_input
        return
      end
      if bar.check_reqs(mob, true)
        mob.start_action SmithingTask.new(3, mob, bar, amt)
      end
    end
    
  end
  
  class SmithingTask < Action
    def initialize(delay, player, product, amt)
      super(player, delay, true)
      @product = product
      @amt = amt
      @count = 0
      mob.get_interface_set.close_window
    end

    def execute
      if @count >= @amt
        mob.cancel_animation
        stop
      return
      end
      if @product.check_reqs mob
        @product.smith mob
        @count += 1
      else
        mob.cancel_animation
        stop
      end
    end

  end
    
  class SmithingInputListener < IntegerScriptInputListener
    def set_context(player, product)
      @player = player
      @product = product
    end

    def inputReceived(value)
      if @product.check_reqs(@player, true)
        if @product.instance_of? Product
          @player.start_action SmithingTask.new(4, @player, @product, value) if value > 0
        elsif @product.instance_of? Bar
          @player.start_action SmithingTask.new(3, @player, @product, value) if value > 0
        end
      end
      @player.get_script_input.reset
    end
  end
  
end

Smithing::create_bars
Smithing::create_types
Smithing::create_type_methods
Smithing::create_products
Smithing::bind_handlers