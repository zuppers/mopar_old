=begin
* Name: pickaxes.rb
* Creates the different pickaxe definitions
* Author: Davidi2 (David Insley)
* Date: November 10, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.model.mob.Animation'

module Mining

  PICKAXES = {}

  class << self
    def create_pickaxe(name, &block)
      PICKAXES[name] = Pickaxe.new block
    end

    def create_pickaxes
      create_pickaxe :bronze do
        @lvl_req = 1
        @animation = Animation.new 625
        @item_id = 1265
        @speed = 1
      end
      create_pickaxe :iron do
        @lvl_req = 1
        @animation = Animation.new 626
        @item_id = 1267
        @speed = 2
      end
      create_pickaxe :steel do
        @lvl_req = 6
        @animation = Animation.new 627
        @item_id = 1269
        @speed = 3
      end
      create_pickaxe :mithril do
        @lvl_req = 21
        @animation = Animation.new 629
        @item_id = 1273
        @speed = 4
      end
      create_pickaxe :adamantite do
        @lvl_req = 31
        @animation = Animation.new 628
        @item_id = 1271
        @speed = 5
      end
      create_pickaxe :runite do
        @lvl_req = 41
        @animation = Animation.new 624
        @item_id = 1275
        @speed = 6
      end
      create_pickaxe :inferno_adze do
        @lvl_req = 41
        @animation = Animation.new 10222
        @item_id = 13661
        @speed = 7
      end
    end

  end

  class Pickaxe
    attr_reader :item_id, :lvl_req, :animation, :speed
    def initialize(block)
      @item_id = 0
      @lvl_req = 99
      @speed = 1
      instance_eval(&block)
    end
  end
end