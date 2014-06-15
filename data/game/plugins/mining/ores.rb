=begin
* Name: ores.rb
* Creates the different ore definitions
* Author: Davidi2 (David Insley)
* Date: November 10, 2013
=end

require 'java'

module Mining

  ORES = {}

  class << self
    def create_ore(name, &block)
      ORES[name] = Ore.new block
    end

    def create_ores
      create_ore :clay do
        @lvl_req = 1
        @xp = 5
        @item_id = 434
        @object_ids = [ 31062, 31063, 15503, 15504, 15505 ]
      end
      create_ore :copper do
        @lvl_req = 1
        @xp = 17.5
        @item_id = 436
        @respawn_seconds = 3
        @object_ids = [ 2090, 2091, 2110, 11189, 11190, 11191, 31080, 31081, 31082, 11936, 11937, 11938, 11960, 11961, 11962 ]
      end
      create_ore :tin do
        @lvl_req = 1
        @xp = 17.5
        @item_id = 438
        @respawn_seconds = 3
        @object_ids = [ 2094, 2311, 37304, 37305, 37306, 11186, 11187, 11188, 2095, 31077, 31078, 31079, 11933, 11934, 11935, 11959, 11958, 11957 ]
      end
      create_ore :blurite do
        @lvl_req = 10
        @xp = 17.5
        @item_id = 668
        @respawn_seconds = 30
        @object_ids = []
      end
      create_ore :iron do
        @lvl_req = 15
        @xp = 35
        @item_id = 440
        @respawn_seconds = 7
        @object_ids = [31071, 31072, 31073, 37309, 37307, 37308, 11954, 11955, 11956 ]
      end
      create_ore :silver do
        @lvl_req = 20
        @xp = 40
        @item_id = 442
        @respawn_seconds = 80
        @object_ids = [ 31074, 31075, 31076, 11948, 11949, 11950 ]
      end
      create_ore :coal do
        @lvl_req = 30
        @xp = 50
        @item_id = 453
        @respawn_seconds = 40
        @object_ids = [ 31068, 31069, 31070, 11932, 11931, 11930 ]
      end
      create_ore :gold do
        @lvl_req = 40
        @xp = 65
        @item_id = 444
        @respawn_seconds = 80
        @object_ids = [ 31065, 31066, 31067, 37310, 37311, 37312, 11951, 11952, 11953 ]
      end
      create_ore :mithril do
        @lvl_req = 55
        @xp = 80
        @item_id = 447
        @respawn_seconds = 120
        @object_ids = [ 31086, 31087, 31088, 11942, 11943, 11944 ]
      end
      create_ore :adamant do
        @lvl_req = 70
        @xp = 95
        @item_id = 449
        @respawn_seconds = 360
        @object_ids = [ 31083, 31084, 31085, 11939, 11940, 11941 ]
      end
      create_ore :runite do
        @lvl_req = 85
        @xp = 125
        @item_id = 451
        @respawn_seconds = 900
        @object_ids = []
      end
    end

  end

  class Ore
    attr_reader :lvl_req, :xp, :item_id, :respawn_ticks, :object_ids
    def initialize(block)
      @lvl_req = 99
      @xp = 0
      @item_id = 0
      @respawn_seconds = 2
      @object_ids = []
      instance_eval(&block)
      @respawn_ticks = @respawn_seconds * 5.0/3.0
    end
  end
end