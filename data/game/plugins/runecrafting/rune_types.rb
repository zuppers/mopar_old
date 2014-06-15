=begin
* Name: runetypes.rb
* Creates the runecrafting definitions
* Author: Davidi2 (David Insley)
* Date: October 14, 2013
=end

require 'java'

module Runecrafting

  RUNE_TYPES = {}

  class << self
    def create_rune_type(name, &block)
      RUNE_TYPES[name] = RuneType.new(block)
    end

    def create_rune_types
      create_rune_type(:air) do
        @item_id = 556
        @lvl_reqs = [1, 11, 22, 33, 44, 55, 66, 77, 88, 99]
        @xp = 5
      end
      create_rune_type(:mind) do
        @item_id = 558
        @lvl_reqs = [1, 14, 28, 42, 56, 70, 84, 98]
        @xp = 5.5
      end
      create_rune_type(:water) do
        @item_id = 555
        @lvl_reqs = [5, 19, 38, 57, 76, 95]
        @xp = 6
      end
      create_rune_type(:earth) do
        @item_id = 557
        @lvl_reqs = [9, 26, 52, 78]
        @xp = 6.5
      end
      create_rune_type(:fire) do
        @item_id = 554
        @lvl_reqs = [14, 35, 70]
        @xp = 7
      end
      create_rune_type(:body) do
        @item_id = 559
        @lvl_reqs = [20, 46, 92]
        @xp = 7.5
      end
      create_rune_type(:cosmic) do
        @item_id = 564
        @lvl_reqs = [27, 59]
        @xp = 8
        @pure = true
      end
      create_rune_type(:chaos) do
        @item_id = 562
        @lvl_reqs = [35, 74]
        @xp = 8.5
        @pure = true
      end
      create_rune_type(:astral) do
        @item_id = 9075
        @lvl_reqs = [40, 82]
        @xp = 8.7
        @pure = true
      end
      create_rune_type(:nature) do
        @item_id = 561
        @lvl_reqs = [44, 91]
        @xp = 9
        @pure = true
      end
      create_rune_type(:law) do
        @item_id = 563
        @lvl_reqs = [54]
        @xp = 9.5
        @pure = true
      end
      create_rune_type(:death) do
        @item_id = 560
        @lvl_reqs = [65]
        @xp = 10
        @pure = true
      end
      create_rune_type(:blood) do
        @item_id = 565
        @lvl_reqs = [77]
        @xp = 10.5
        @pure = true
      end
    end

  end

  class RuneType
    attr_reader :item_id, :lvl_reqs, :xp, :pure
    def initialize(block)
      @item_id = 1      #The crafted rune ID
      @lvl_reqs = [99] #The level requirements for this rune type... [0] is for 1 per ess, [1] is for 2 per ess, etc
      @xp = 0           #XP per ess craft (Reminder: Crafting multiples does NOT grant bonus XP)
      @pure = false     #Requires pure ess? No by default
      instance_eval(&block)
    end
    
    def get_rune_multiplier(lvl)
      for i in 0...@lvl_reqs.length 
        return i if lvl < @lvl_reqs[i]
      end
      return @lvl_reqs.length
    end
  end

end