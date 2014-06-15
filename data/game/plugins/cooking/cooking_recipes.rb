=begin
* Name: cooking_recipe.rb
* Handles cooking recipes (item on item)
* Author: Davidi2 (David Insley)
* Date: November 20, 2013
=end

require 'java'

java_import 'net.scapeemulator.game.model.mob.Animation'
java_import 'net.scapeemulator.game.model.player.skills.Skill'
java_import 'net.scapeemulator.game.model.player.Item'
java_import 'net.scapeemulator.game.item.ItemOnItemDispatcher'

module Cooking

  RECIPES = {}
  
  WATER_SOURCES = {1929 => 1925, 1937 => 1935}
  POT_OF_FLOUR = 1933
  TOMATO = 
  CHEESE = 
  
  class << self
    def create_recipe(name, &block)
      recipe = Recipe.new(block)
      if RECIPES[recipe.hash].nil?
        RECIPES[recipe.hash] = Hash.new
      end
      RECIPES[recipe.hash][name] = recipe
    end

    def create_recipes
      WATER_SOURCES.each do |water_id, return_id|
        create_recipe :bread_dough do
          @lvl_req = 1
          @xp = 1
          @primary_id = POT_OF_FLOUR
          @secondary_id = water_id
          @product_id = 2307
          @return_id = return_id
          @message = 'You mix the water and flour to make some bread dough.'
        end
        create_recipe :pitta_dough do
          @lvl_req = 58
          @xp = 1
          @primary_id = POT_OF_FLOUR
          @secondary_id = water_id
          @product_id = 1863
          @return_id = return_id
          @message = 'You mix the water and flour to make some pitta dough.'
        end
        create_recipe :pastry_dough do
          @lvl_req = 10
          @xp = 1
          @primary_id = POT_OF_FLOUR
          @secondary_id = water_id
          @product_id = 1953
          @return_id = return_id
          @message = 'You mix the water and flour to make some pastry dough.'
        end
        create_recipe :pizza_base do
          @lvl_req = 35
          @xp = 1
          @primary_id = POT_OF_FLOUR
          @secondary_id = water_id
          @product_id = 2283
          @return_id = return_id
          @message = 'You mix the water and flour to make a pizza base.'
        end
      end
      create_recipe :incomplete_pizza do
        @lvl_req = 35
        @xp = 1
        @primary_id = 2283
        @secondary_id = TOMATO
        @product_id = 2285
      end
      create_recipe :uncooked_pizza do
        @lvl_req = 35
        @xp = 1
        @primary_id = 2285
        @secondary_id = CHEESE
        @product_id = 2287
      end
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
      @secondary_id = 0
      @return_id = 0 #For example returning a pot when doing bucket of water on a pot of flour
      @product_id = 0
      @message = 'COOKING PLACEHOLDER'
      instance_eval(&block)
      @hash = ItemOnItemDispatcher::calculate_hash(primary_id, secondary_id)
    end

    def check_reqs(player)
      if player.get_skill_set.get_current_level(Skill::COOKING) < @lvl_req
        player.send_message "You need a Cooking level of #{@lvl_req} to make that."
        return false
      end
      true
    end

    def combine(player)
      player.get_inventory.remove Item.new(@primary_id) if @primary_id > 0
      player.get_inventory.remove Item.new(@secondary_id) if @secondary_id > 0
      player.get_inventory.add Item.new(@product_id) if @product_id > 0
      player.get_inventory.add Item.new(@return_id) if @return_id > 0
      player.get_skill_set.add_experience(Skill::COOKING, @xp) if @xp > 0
      player.send_message @message
    end
    
  end

end