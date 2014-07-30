=begin
* Name: bars.rb
* Creates the different smithing bars definitions
* Author: Davidi2 (David Insley)
* Date: November 11, 2013
=end

require 'java'

module Smithing

  BARS = {}

  class << self
    def create_bar(name, &block)
      BARS[name] = Bar.new block
      SMITHING_TABLES[BARS[name]] = []
    end

    def create_bars
      create_bar :bronze do
        @smelt_lvl_req = 1
        @smelt_xp = 6.2
        @bar_id = 2349
        @primary_ore = 436
        @secondary_ore = 438
        @secondary_amt = 1
        @smith_xp = 12.5
      end
      create_bar :blurite do
        @smelt_lvl_req = 8
        @smelt_xp = 8
        @bar_id = 9467
        @primary_ore = 668
      end
      create_bar :iron do
        @smelt_lvl_req = 15
        @smelt_xp = 12.5
        @bar_id = 2351
        @primary_ore = 440
        @smith_xp = 25
      end
      create_bar :silver do
        @smelt_lvl_req = 20
        @smelt_xp = 13.7
        @bar_id = 2355
        @primary_ore = 442
      end
      create_bar :steel do
        @smelt_lvl_req = 30
        @smelt_xp = 17.5
        @bar_id = 2353
        @primary_ore = 440
        @secondary_amt = 2
        @smith_xp = 37.5
      end
      create_bar :gold do
        @smelt_lvl_req = 40
        @smelt_xp = 22.5
        @bar_id = 2357
        @primary_ore = 444
      end
      create_bar :mithril do
        @smelt_lvl_req = 50
        @smelt_xp = 17.5
        @bar_id = 2359
        @primary_ore = 447
        @secondary_amt = 4
        @smith_xp = 50
      end
      create_bar :adamant do
        @smelt_lvl_req = 70
        @smelt_xp = 37.5
        @bar_id = 2361
        @primary_ore = 449
        @secondary_amt = 6
        @smith_xp = 62.5
      end
      create_bar :rune do
        @smelt_lvl_req = 85
        @smelt_xp = 50
        @bar_id = 2363
        @primary_ore = 451
        @secondary_amt = 8
        @smith_xp = 75
      end
    end

  end

  class Bar
    attr_reader :smelt_lvl_req, :smelt_xp, :bar_id, :primary_ore, :primary_amt, :secondary_ore, :secondary_amt, :smith_xp
    def initialize(block)
      @smelt_lvl_req = 99
      @smelt_xp = 0
      @bar_id = 0
      @primary_ore = nil
      @primary_amt = 1
      @secondary_ore = 453
      @secondary_amt = 0
      @smith_xp = 0
      instance_eval(&block)
    end
    
    def check_reqs(player, first=false)
      if player.get_skill_set.get_current_level(Skill::SMITHING) < @smelt_lvl_req
        player.send_message "You need a Smithing level of #{@smelt_lvl_req} to smelt that."
        return false
      end
      if (player.get_inventory.get_amount(@secondary_ore) < @secondary_amt) || (player.get_inventory.get_amount(@primary_ore) < @primary_amt)
        if !first
          player.send_message 'You have run out of ores to smelt.'
        else
          player.send_message 'You do not have the ores required to smelt that.'
        end
        return false
      end
      true
    end

    def smith(player)
      player.get_inventory.remove Item.new(@primary_ore, @primary_amt)
      player.get_inventory.remove Item.new(@secondary_ore, @secondary_amt) if @secondary_amt > 0
      bar_item = Item.new @bar_id
      player.get_inventory.add bar_item
      player.play_animation SMELTING_ANIMATION
      player.get_skill_set.add_experience(Skill::SMITHING, @smelt_xp)
      player.send_message "You smelt a #{bar_item.definition.name.downcase}."
    end
  end
end