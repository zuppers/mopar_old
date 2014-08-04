=begin
* Name: recipe.rb
* Handles the way fletching recipes work
* Author: Davidi2 (David Insley)
* Date: October 26, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.model.mob.Animation'
java_import 'net.scapeemulator.game.model.player.skills.Skill'
java_import 'net.scapeemulator.game.model.player.Item'
java_import 'net.scapeemulator.game.dispatcher.item.ItemOnItemDispatcher'

module Fletching

  RECIPES = {}
  FEATHER = 314
  KNIFE = 946
  BOW_STRING = 1777
  ARROW_SHAFT = 52
  HEADLESS_ARROW = 53

  class << self
    def create_recipe(name, &block)
      recipe = Recipe.new(block)
      if RECIPES[recipe.hash].nil?
        RECIPES[recipe.hash] = Hash.new
      end
      RECIPES[recipe.hash][name] = recipe
    end

    def create_recipes
      create_bows
      create_arrows
    end

    def get_recipe(r_name)
      RECIPES.each do |hash, recipes|
        recipes.each do |name, recipe|
          return recipe if r_name == name
        end
      end
    end

  end

  class Recipe
    attr_reader :lvl_req, :xp, :primary_id, :secondary_id, :product_id, :hash, :delay
    def initialize(block)
      @lvl_req = 99
      @xp = 0
      @primary_id = 0
      @primary_amt = 1
      @secondary_id = 0
      @secondary_amt = 1
      @consume_primary = true
      @consume_secondary = true
      @product_id = 0
      @product_amt = 1
      @animation_id = 0
      @message = 'FLETCHING PLACEHOLDER'
      @delay = 2
      instance_eval(&block)
      @hash = ItemOnItemDispatcher::calculate_hash(primary_id, secondary_id)
    end

    def check_reqs(player)
      if player.get_skill_set.get_current_level(Skill::FLETCHING) < @lvl_req
        player.send_message "You need a Fletching level of #{@lvl_req} to do that."
      return false
      end
      if (player.get_inventory.get_amount(@primary_id) < @primary_amt) || (player.get_inventory.get_amount(@secondary_id) < @secondary_amt)
        player.send_message "You do not have the items required to fletch that many."
      return false
      end
      true
    end

    def fletch(player)
      player.get_inventory.remove Item.new(@primary_id, @primary_amt) if @consume_primary
      player.get_inventory.remove Item.new(@secondary_id, @secondary_amt) if @consume_secondary
      player.get_inventory.add Item.new(@product_id, @product_amt)
      player.play_animation Animation.new @animation_id if @animation_id > 0
      player.get_skill_set.add_experience(Skill::FLETCHING, @xp)
      player.send_message @message
    end

    def calculate_max(player)
      count1 = player.get_inventory.get_amount(@primary_id)/@primary_amt
      count2 = player.get_inventory.get_amount(@secondary_id)/@secondary_amt
      return count1 if count1 < count2
      count2
    end
  end

end