=begin
* Name: arrows.rb
* Creates the fletching arrow definitions
* Author: Davidi2 (David Insley)
* Date: October 28, 2013
=end

require 'java'

module Fletching
  class << self
    def create_arrow(name, lvl_req, xp, tip_id, arrow_id)
      create_recipe(name) do
        @lvl_req = lvl_req
        @xp = xp
        @primary_id = HEADLESS_ARROW
        @primary_amt = 15
        @secondary_id = tip_id
        @secondary_amt = 15
        @product_id = arrow_id
        @product_amt = 15
        @message = "You attach tips to create 15 arrows."
        @animation_id = -1
      end
    end

    def create_arrows
      create_recipe(:arrow_shaft) do
        @lvl_req = 1
        @xp = 5
        @primary_id = KNIFE
        @consume_primary = false
        @secondary_id = 1511
        @product_id = ARROW_SHAFT
        @product_amt = 15
        @message = "You carefully cut the wood into 15 arrow shafts."
        @animation_id = 1248
      end
      create_recipe(:headless_arrow) do
        @lvl_req = 1
        @xp = 15
        @primary_id = ARROW_SHAFT
        @primary_amt = 15
        @secondary_id = FEATHER
        @secondary_amt = 15
        @product_id = HEADLESS_ARROW
        @product_amt = 15
        @message = "You attach feathers to 15 arrow shafts."
        @animation_id = -1
      end
      create_arrow(:bronze_arrow, 1, 19.5, 39, 882)
      create_arrow(:iron_arrow, 15, 37.5, 40, 884)
      create_arrow(:steel_arrow, 30, 75, 41, 886)
      create_arrow(:mithril_arrow, 45, 112.5, 42, 888)
      create_arrow(:adamant_arrow, 60, 150, 43, 890)
      create_arrow(:rune_arrow, 75, 187.4, 44, 892)
      create_arrow(:dragon_arrow, 90, 224.5, 11237, 11212)
    end

  end
end