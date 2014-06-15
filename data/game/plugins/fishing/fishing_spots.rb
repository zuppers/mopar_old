=begin
* Name: fishing_spots.rb
* Creates the different fishing spot definitions
* Author: Davidi2 (David Insley) 
* [Special thanks to xxx123xxx and latifundio for some of the spot coords]
* Date: November 2, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.model.player.Item'

module Fishing

  SPOTS = {}

  class << self
    def create_spot(name, &block)
      SPOTS[name] = FishingSpot.new(block)
    end

    def create_spots
      create_spot :smallnet_and_rod do
        @npc_id = 316
        @tools = [TOOLS[:small_net], TOOLS[:fishing_rod]]
        @first_types = [FISH[:shrimp], FISH[:anchovy]]
        @second_types = [FISH[:sardine], FISH[:herring]]
      end
      create_spot :flyrod_and_rod do
        @npc_id = 309
        @tools = [TOOLS[:fly_fishing_rod], TOOLS[:fishing_rod]]
        @first_types = [FISH[:trout], FISH[:salmon]]
        @second_types = [FISH[:pike]]
      end
      create_spot :cage_and_harpoon do
        @npc_id = 312
        @tools = [TOOLS[:lobster_cage], TOOLS[:harpoon]]
        @first_types = [FISH[:lobster]]
        @second_types = [FISH[:tuna], FISH[:swordfish]]
      end
      create_spot :bignet_and_harpoon do
        @npc_id = 322
        @tools = [TOOLS[:big_net], TOOLS[:harpoon]]
        @first_types = [FISH[:mackerel], FISH[:cod], FISH[:bass]]
        @second_types = [FISH[:shark]]
      end
      create_spot :harpoon_and_smallnet do
        @npc_id = 400
        @tools = [TOOLS[:harpoon], TOOLS[:small_net]]
        @first_types = [FISH[:tuna], FISH[:swordfish]]
        @second_types = [FISH[:monkfish]]
      end
      spawn_spots
    end

    def spawn_spot(spot_type, x, y)
      create_npc :normal, :id => SPOTS[spot_type].npc_id, :position => Position.new(x, y)
    end

    def spawn_spots
      # Catherby fishing spots
      spawn_spot(:cage_and_harpoon, 2836, 3431)
      spawn_spot(:bignet_and_harpoon, 2837, 3431)
      spawn_spot(:cage_and_harpoon, 2838, 3431)
      spawn_spot(:bignet_and_harpoon, 2844, 3429)
      spawn_spot(:cage_and_harpoon, 2845, 3429)
      spawn_spot(:cage_and_harpoon, 2859, 3426)
      spawn_spot(:smallnet_and_rod, 2853, 3423)
      spawn_spot(:smallnet_and_rod, 2845, 3423)
      spawn_spot(:smallnet_and_rod, 2855, 3423)
      spawn_spot(:smallnet_and_rod, 2860, 3426)

      # Musa point pier fishing spots
      spawn_spot(:cage_and_harpoon, 2925, 3181)
      spawn_spot(:cage_and_harpoon, 2926, 3180)
      spawn_spot(:cage_and_harpoon, 2926, 3179)
      spawn_spot(:cage_and_harpoon, 2923, 3179)
      spawn_spot(:smallnet_and_rod, 2921, 3178)
      spawn_spot(:smallnet_and_rod, 2924, 3181)
      spawn_spot(:smallnet_and_rod, 2923, 3180)

      # Fishing guild spots
      spawn_spot(:smallnet_and_rod, 2602, 3414)
      spawn_spot(:smallnet_and_rod, 2602, 3422)
      spawn_spot(:cage_and_harpoon, 2604, 3417)
      spawn_spot(:cage_and_harpoon, 2605, 3420)
      spawn_spot(:bignet_and_harpoon, 2612, 3411)
      spawn_spot(:bignet_and_harpoon, 2612, 3415)

      # Draynor village spots
      spawn_spot(:smallnet_and_rod, 3085, 3231)
      spawn_spot(:smallnet_and_rod, 3085, 3230)
      
      # Barbarian village spots
      spawn_spot(:flyrod_and_rod, 3104, 3424)
      spawn_spot(:flyrod_and_rod, 3104, 3425)
      spawn_spot(:flyrod_and_rod, 3110, 3432)
      spawn_spot(:flyrod_and_rod, 3110, 3433)
      spawn_spot(:flyrod_and_rod, 3110, 3434)

      # Relleka spots
      spawn_spot(:smallnet_and_rod, 2633, 3691)
      spawn_spot(:smallnet_and_rod, 2633, 3694)
      spawn_spot(:cage_and_harpoon, 2639, 3698)
      spawn_spot(:cage_and_harpoon, 2640, 3700)
      spawn_spot(:bignet_and_harpoon, 2645, 3708)
      spawn_spot(:bignet_and_harpoon, 2649, 3708)

      # Barbarian outpost spots (different than heavy rod spots)
      spawn_spot(:smallnet_and_rod, 2498, 3545)
      spawn_spot(:smallnet_and_rod, 2511, 3562)
      spawn_spot(:smallnet_and_rod, 2516, 3575)

      # Rimmington spots
      spawn_spot(:smallnet_and_rod, 2986, 3176)
      spawn_spot(:smallnet_and_rod, 2997, 3159)

      # Al-Kharid spots
      spawn_spot(:smallnet_and_rod, 3267, 3148)
      spawn_spot(:smallnet_and_rod, 3275, 3140)

      # Seers village spots
      spawn_spot(:flyrod_and_rod, 2716, 3530)
      spawn_spot(:flyrod_and_rod, 2726, 3524)

      # Random river spots north of Ardougne
      spawn_spot(:flyrod_and_rod, 2508, 3421)
      spawn_spot(:flyrod_and_rod, 2527, 3412)
      spawn_spot(:flyrod_and_rod, 2537, 3406)
      spawn_spot(:flyrod_and_rod, 2562, 3374)
      spawn_spot(:flyrod_and_rod, 2566, 3370)

      # River north east of castle wars (east of observ.) spots
      spawn_spot(:flyrod_and_rod, 2461, 3150)
      spawn_spot(:flyrod_and_rod, 2465, 3156)

      # Shilo village spots
      spawn_spot(:flyrod_and_rod, 2855, 2974)
      spawn_spot(:flyrod_and_rod, 2855, 2977)
      spawn_spot(:flyrod_and_rod, 2859, 2976)
      spawn_spot(:flyrod_and_rod, 2860, 2972)
      spawn_spot(:flyrod_and_rod, 2864, 2975)

      # Wilderness spots
      spawn_spot(:cage_and_harpoon, 3347, 3814)
      spawn_spot(:cage_and_harpoon, 3364, 3800)

      # Lumbridge spots
      spawn_spot(:flyrod_and_rod, 3228, 3252)
      spawn_spot(:flyrod_and_rod, 3239, 3241)
      spawn_spot(:flyrod_and_rod, 3239, 3243)
      spawn_spot(:flyrod_and_rod, 3238, 3253)
    end
  end

  class FishingSpot
    attr_reader :npc_id, :tools, :first_types, :second_types
    def initialize(block)
      @npc_id = -1
      @tools = []
      @first_tool = -1
      @second_tool = -1
      @first_types = []
      @second_types = []
      instance_eval(&block)
    end

    def check_reqs(player, option)
      types = option == 1 ? @first_types : @second_types
      tool = tools[option-1]
      lvl_req = 99
      for fish in types
        lvl_req = fish.lvl_req if fish.lvl_req < lvl_req
      end
      if player.get_skill_set.get_current_level(Skill::FISHING) < lvl_req
        player.send_message "You need a Fishing level of #{lvl_req} to fish there."
        return false
      end
      if player.get_inventory.get_amount(tool.item_id) < 1
        player.send_message "You need a #{Item.new(tool.item_id).definition.name.downcase} to fish there."
        return false
      end
      if tool.bait_id > 0
        if player.get_inventory.get_amount(tool.bait_id) < 1
          player.send_message 'You do not have the bait required to fish there.'
            return false
        end
      end
      if player.get_inventory.free_slots < 1
        player.send_message 'Your inventory is too full to hold any more fish.'
        return false
      end
      true
    end

    def attempt_catch(player, option)
      types = option == 1 ? @first_types : @second_types
      tool = tools[option-1]
      lvl_req = 99
      plyr_lvl = player.get_skill_set.get_current_level Skill::FISHING
      for fish in types
        lvl_req = fish.lvl_req if fish.lvl_req < lvl_req
      end
      chance = (plyr_lvl - lvl_req) * 2 + 20
      chance = 80 if chance > 80
      should_catch = chance > rand(100)

      if should_catch
        can_catch = []
        for fish in types
          can_catch.push fish if fish.lvl_req <= plyr_lvl
        end
        type = can_catch.sample
        caught = Item.new type.item_id
        player.get_inventory.remove Item.new(tool.bait_id) if tool.bait_id > 0
        player.get_inventory.add caught
        caught_name = caught.definition.name.downcase
        conj = caught_name.end_with?('s') ? 'some' : 'a'
        player.send_message "You manage to catch #{conj} #{caught_name}."
        player.get_skill_set.add_experience(Skill::FISHING, type.xp)
      end

    end
  end
end