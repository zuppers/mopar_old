=begin
* Name: fish.rb
* Creates the different fish definitions
* Author: Davidi2 (David Insley)
* Date: November 2, 2013
=end

require 'java'

module Fishing

  FISH = {}

  class << self
    def create_fish(name, &block)
      FISH[name] = Fish.new(block)
    end

    def create_fish_defs
      create_fish :shrimp do
        @lvl_req = 1
        @xp = 10
        @item_id = 317
      end
      create_fish :sardine do
        @lvl_req = 5
        @xp = 20
        @item_id = 327
      end
      create_fish :herring do
        @lvl_req = 10
        @xp = 30
        @item_id = 345
      end
      create_fish :anchovy do
        @lvl_req = 15
        @xp = 40
        @item_id = 321
      end
      create_fish :mackerel do
        @lvl_req = 16
        @xp = 20
        @item_id = 353
      end
      create_fish :trout do
        @lvl_req = 20
        @xp = 50
        @item_id = 335
      end
      create_fish :cod do
        @lvl_req = 23
        @xp = 45
        @item_id = 3341
      end
      create_fish :pike do
        @lvl_req = 25
        @xp = 60
        @item_id = 349
      end
      create_fish :salmon do
        @lvl_req = 30
        @xp = 70
        @item_id = 331
      end
      create_fish :tuna do
        @lvl_req = 35
        @xp = 80
        @item_id = 359
      end
      create_fish :lobster do
        @lvl_req = 40
        @xp = 90
        @item_id = 377
      end
      create_fish :bass do
        @lvl_req = 46
        @xp = 100
        @item_id = 363
      end
      create_fish :swordfish do
        @lvl_req = 50
        @xp = 100
        @item_id = 371
      end
      create_fish :monkfish do
        @lvl_req = 62
        @xp = 120
        @item_id = 7944
      end
      create_fish :shark do
        @lvl_req = 76
        @xp = 110
        @item_id = 383
      end
      
    end

  end

  class Fish
    attr_reader :lvl_req, :xp, :item_id
    def initialize(block)
      @lvl_req = 99
      @xp = 0
      @item_id = 0
      instance_eval(&block)
    end
  end
end