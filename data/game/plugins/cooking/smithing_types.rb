=begin
* Name: smithing_types.rb
* Creates the different smithing type definitions
* Author: Davidi2 (David Insley)
* Date: November 11, 2013
=end

require 'java'

module Smithing

  TYPES = {}

  class << self
    def create_type(name, &block)
      TYPES[name] = Type.new name, block
    end

    def create_types
      create_type :dagger do
        @interface_offset = 18
      end
      create_type :axe do
        @interface_offset = 26
      end
      create_type :mace do
        @interface_offset = 34
      end
      create_type :medium_helm do
        @interface_offset = 42
      end
      create_type :crossbow_bolts do
        @amount = 10
        @interface_offset = 50
      end
      create_type :sword do
        @interface_offset = 58
      end
      create_type :dart_tips do
        @amount = 10
        @interface_offset = 66
      end
      create_type :nails do
        @amount = 15
        @interface_offset = 74
      end
=begin      
      create_type :wire do
        # TODO ????
        @interface_offset = 82
      end
      create_type :studs do
        # TODO ????
        @interface_offset = 82
      end
=end
      create_type :arrow_tips do
        @amount = 15
        @interface_offset = 106
      end
      create_type :scimitar do
        @bars = 2
        @interface_offset = 114
      end
=begin      create_type :pickaxe do
        @bars = 2
        @interface_offset = 18
=end      end
      create_type :crossbow_limbs do
        @interface_offset = 122
      end
      create_type :longsword do
        @bars = 2
        @interface_offset = 130
      end
      create_type :throwing_knife do
        @amount = 5
        @interface_offset = 138
      end
      create_type :full_helm do
        @bars = 2
        @interface_offset = 146
      end
      create_type :square_shield do
        @bars = 2
        @interface_offset = 154
      end
      ##EMPTY 1
      ##EMPTY 2
      create_type :warhammer do
        @bars = 3
        @interface_offset = 178
      end
      create_type :battleaxe do
        @bars = 3
        @interface_offset = 186
      end
      create_type :chainbody do
        @bars = 3
        @interface_offset = 194
      end
      create_type :kiteshield do
        @bars = 3
        @interface_offset = 202
      end
      create_type :claws do
        @bars = 2
        @interface_offset = 210
      end
      create_type '2h_sword'.to_sym do
        @bars = 3
        @interface_offset = 218
      end
      create_type :plateskirt do
        @bars = 3
        @interface_offset = 226
      end
      create_type :platelegs do
        @bars = 3
        @interface_offset = 234
      end
      create_type :platebody do
        @bars = 5
        @interface_offset = 242
      end
    end

  end

  class Type
    #Interface offset: +0 = item on interface
    # =>               +1 = item type (shouldnt need to change)
    # =>               +2 = bar count
    # =>               +3-6 = options (ALL X 5 1)
    attr_reader :bars, :amount, :interface_offset, :name
    def initialize(name, block)
      @bars = 1 #How many bars are needed?
      @amount = 1 #How many of the item are we going to give them?
      @interface_offset = 0 #Where does this type show on the smithing window?
      @name = name.to_s.capitalize.gsub '_', ' '
      instance_eval(&block)
    end
  end
end