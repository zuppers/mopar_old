=begin
* Name: bows.rb
* Creates the fletching bow definitions
* Author: Davidi2 (David Insley)
* Date: October 28, 2013
=end

require 'java'

module Fletching

  class << self
   
    def create_bow_u(name, lvl_req, xp, log_id, bow_u_id)
      create_recipe(name) do
        @lvl_req = lvl_req
        @xp = xp
        @primary_id = KNIFE
        @consume_primary = false
        @secondary_id = log_id
        @product_id = bow_u_id
        @message = "You carefully cut the wood into a bow."
        @animation_id = 1248
        @delay = 3
      end
    end
    
    def create_bow(name, unstrung_name, bow_id, animation_id)
      unstrung = get_recipe unstrung_name
      create_recipe(name) do
        @lvl_req = unstrung.lvl_req
        @xp = unstrung.xp
        @primary_id = BOW_STRING
        @secondary_id = unstrung.product_id
        @product_id = bow_id
        @message = "You add a string to the bow."
        @animation_id = animation_id
      end
    end
    
    def create_bows
      create_bow_u(:shortbow_u, 5, 5, 1511, 50)
      create_bow(:shortbow, :shortbow_u, 841, 6678)
      
      create_bow_u(:longbow_u, 10, 10, 1511, 48)
      create_bow(:longbow, :longbow_u, 839, 6684)
      
      create_bow_u(:oak_shortbow_u, 20, 16.5, 1521, 54)
      create_bow(:oak_shortbow, :oak_shortbow_u, 843, 6679)
      
      create_bow_u(:oak_longbow_u, 25, 25, 1521, 56)
      create_bow(:oak_longbow, :oak_longbow_u, 845, 6685)
      
      create_bow_u(:willow_shortbow_u, 35, 33.3, 1519, 60)
      create_bow(:willow_shortbow, :willow_shortbow_u, 849, 6680)
      
      create_bow_u(:willow_longbow_u, 40, 41.5, 1519, 58)
      create_bow(:willow_longbow, :willow_longbow_u, 848, 6686)
      
      create_bow_u(:maple_shortbow_u, 50, 50, 1517, 64)
      create_bow(:maple_shortbow, :maple_shortbow_u, 853, 6681)
      
      create_bow_u(:maple_longbow_u, 55, 58.3, 1517, 62)
      create_bow(:maple_longbow, :maple_longbow_u, 851, 6687)
      
      create_bow_u(:yew_shortbow_u, 65, 67.5, 1515, 68)
      create_bow(:yew_shortbow, :yew_shortbow_u, 857, 6682)
      
      create_bow_u(:yew_longbow_u, 70, 75, 1515, 66)
      create_bow(:yew_longbow, :yew_longbow_u, 855, 6688)
      
      create_bow_u(:magic_shortbow_u, 80, 83.3, 1513, 72)
      create_bow(:magic_shortbow, :magic_shortbow_u, 861, 6683)
      
      create_bow_u(:magic_longbow_u, 85, 91.5, 1513, 70)
      create_bow(:magic_longbow, :magic_longbow_u, 859, 6689)
    end
    
  end
end