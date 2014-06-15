=begin
* Name: logs.rb
* Creates the firemaking log definitions
* Author: Davidi2 (David Insley)
* Date: October 3, 2013
=end

require 'java'

module Firemaking

  LOGS = {}

  class << self
    def create_log(name, &block)
      LOGS[name] = Log.new block
    end

    def create_logs
      create_log :normal do
        @item_id = 1511
        @lvl_req = 1
        @xp = 40
      end
      create_log :achey do
        @item_id = 2862
        @lvl_req = 1
        @xp = 40
      end
      create_log :oak do
        @item_id = 1521
        @lvl_req = 15
        @xp = 60
      end
      create_log :willow do
        @item_id = 1519
        @lvl_req = 30
        @xp = 90
      end
      create_log :teak do
        @item_id = 6333
        @lvl_req = 35
        @xp = 105
      end
      create_log :maple do
        @item_id = 1517
        @lvl_req = 45
        @xp = 135
      end
      create_log :mahogany do
        @item_id = 6332
        @lvl_req = 50
        @xp = 157.5
      end
      create_log :arctic_pine do
        @item_id = 10810
        @lvl_req = 54
        @xp = 175
      end
      create_log :eucalyptus do
        @item_id = 12581
        @lvl_req = 58
        @xp = 193.5
      end
      create_log :yew do
        @item_id = 1515
        @lvl_req = 60
        @xp = 202.5
      end
      create_log :magic do
        @item_id = 1513
        @lvl_req = 75
        @xp = 303.8
      end
    end

  end

  class Log
    attr_reader :item_id, :lvl_req, :xp, :fire_id
    def initialize(block)
      @item_id = 1
      @lvl_req = 1
      @xp = 0
      @fire_id = 2732
      instance_eval(&block)
    end
  end

end