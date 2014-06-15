=begin
* Name: fishing_tools.rb
* Creates the different fishing tool definitions
* Author: Davidi2 (David Insley)
* Date: November 2, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.model.mob.Animation'
java_import 'net.scapeemulator.game.model.player.skills.Skill'
java_import 'net.scapeemulator.game.item.ItemOnItemDispatcher'

module Fishing

  TOOLS = {}

  class << self
    def create_tool(name, &block)
      TOOLS[name] = FishingTool.new(block)
    end

    def create_tools
      create_tool :small_net do
        @item_id = 303
        @animation_id = 620
        @message = 'You cast out your net...'
      end
      create_tool :big_net do
        @item_id = 305
        @animation_id = 620
        @message = 'You cast out your large net...'
      end
      create_tool :fishing_rod do
        @item_id = 307
        @bait_id = 313
        @animation_id = 622
        @message = 'You cast out your fishing rod...'
      end
      create_tool :fly_fishing_rod do
        @item_id = 309
        @bait_id = 314
        @animation_id = 622
        @message = 'You cast out your fly fishing rod...'
      end
      create_tool :harpoon do
        @item_id = 311
        @animation_id = 618
        @message = 'You begin to fish with the harpoon...'
      end
      create_tool :lobster_cage do
        @item_id = 301
        @animation_id = 619
        @message = 'You lower the cage into the water...'
      end
    end

  end

  class FishingTool
    attr_reader :item_id, :bait_id, :animation_id, :message
    def initialize(block)
      @item_id = -1
      @bait_id = -1
      @animation_id = -1
      @message = 'FISHING PLACEHOLDER'
      instance_eval(&block)
    end
  end
end