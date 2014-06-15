=begin
* Name: smithing_products.rb
* Creates the different smithing product definitions
* Author: Davidi2 (David Insley)
* Date: November 11, 2013
=end

require 'java'

module Smithing
  class << self
    def create_product(bar, type, lvl_req, item_id)
      SMITHING_TABLES[bar] << Product.new(bar, type, lvl_req, item_id)
    end

    def create_type_methods
      TYPES.each do |name, type|
        define_singleton_method("create_#{name}") do |bar_name, lvl_req, item_id|
          create_product(BARS[bar_name], type, lvl_req, item_id)
        end
      end
    end

    def create_products
      create_dagger :bronze, 1, 1205
      create_dagger :iron, 15, 1203
      create_dagger :steel, 30, 1207
      create_dagger :mithril, 50, 1209
      create_dagger :adamant, 70, 1211
      create_dagger :rune, 85, 1213

      create_axe :bronze, 1, 1351
      create_axe :iron, 16, 1349
      create_axe :steel, 31, 1353
      create_axe :mithril, 51, 1355
      create_axe :adamant, 71, 1357
      create_axe :rune, 86, 1359

      create_mace :bronze, 2, 1422
      create_mace :iron, 17, 1420
      create_mace :steel, 32, 1424
      create_mace :mithril, 52, 1428
      create_mace :adamant, 72, 1430
      create_mace :rune, 87, 1432

      create_medium_helm :bronze, 3, 1139
      create_medium_helm :iron, 18, 1137
      create_medium_helm :steel, 33, 1141
      create_medium_helm :mithril, 53, 1143
      create_medium_helm :adamant, 73, 1145
      create_medium_helm :rune, 88, 1147

      create_crossbow_bolts :bronze, 3, 9375
      create_crossbow_bolts :iron, 18, 9377
      create_crossbow_bolts :steel, 33, 9378
      create_crossbow_bolts :mithril, 53, 9379
      create_crossbow_bolts :adamant, 73, 9380
      create_crossbow_bolts :rune, 88, 9381

      create_sword :bronze, 4, 1277
      create_sword :iron, 19, 1279
      create_sword :steel, 34, 1281
      create_sword :mithril, 54, 1285
      create_sword :adamant, 74, 1287
      create_sword :rune, 89, 1289

      create_dart_tips :bronze, 4, 819
      create_dart_tips :iron, 19, 820
      create_dart_tips :steel, 34, 821
      create_dart_tips :mithril, 54, 822
      create_dart_tips :adamant, 74, 823
      create_dart_tips :rune, 89, 824

      create_nails :bronze, 4, 4819
      create_nails :iron, 19, 4820
      create_nails :steel, 34, 1539
      create_nails :mithril, 54, 4822
      create_nails :adamant, 74, 4823
      create_nails :rune, 89, 4824

      create_arrow_tips :bronze, 5, 39
      create_arrow_tips :iron, 20, 40
      create_arrow_tips :steel, 35, 41
      create_arrow_tips :mithril, 55, 42
      create_arrow_tips :adamant, 75, 43
      create_arrow_tips :rune, 90, 44

      create_scimitar :bronze, 5, 1321
      create_scimitar :iron, 20, 1323
      create_scimitar :steel, 35, 1325
      create_scimitar :mithril, 55, 1329
      create_scimitar :adamant, 75, 1331
      create_scimitar :rune, 90, 1333

      create_crossbow_limbs :bronze, 6, 9420
      create_crossbow_limbs :iron, 23, 9423
      create_crossbow_limbs :steel, 36, 9425
      create_crossbow_limbs :mithril, 56, 9427
      create_crossbow_limbs :adamant, 76, 9429
      create_crossbow_limbs :rune, 91, 9431

      create_longsword :bronze, 6, 1291
      create_longsword :iron, 21, 1293
      create_longsword :steel, 36, 1295
      create_longsword :mithril, 56, 1299
      create_longsword :adamant, 76, 1301
      create_longsword :rune, 91, 1303

      create_throwing_knife :bronze, 7, 864
      create_throwing_knife :iron, 22, 863
      create_throwing_knife :steel, 37, 865
      create_throwing_knife :mithril, 57, 866
      create_throwing_knife :adamant, 77, 867
      create_throwing_knife :rune, 92, 868

      create_full_helm :bronze, 7, 1155
      create_full_helm :iron, 22, 1153
      create_full_helm :steel, 37, 1157
      create_full_helm :mithril, 57, 1159
      create_full_helm :adamant, 77, 1161
      create_full_helm :rune, 92, 1163

      create_square_shield :bronze, 8, 1173
      create_square_shield :iron, 23, 1175
      create_square_shield :steel, 38, 1177
      create_square_shield :mithril, 58, 1181
      create_square_shield :adamant, 78, 1183
      create_square_shield :rune, 93, 1185

      create_warhammer :bronze, 9, 1337
      create_warhammer :iron, 24, 1335
      create_warhammer :steel, 39, 1339
      create_warhammer :mithril, 59, 1343
      create_warhammer :adamant, 79, 1345
      create_warhammer :rune, 94, 1347

      create_battleaxe :bronze, 10, 1375
      create_battleaxe :iron, 24, 1363
      create_battleaxe :steel, 40, 1365
      create_battleaxe :mithril, 60, 1369
      create_battleaxe :adamant, 80, 1371
      create_battleaxe :rune, 95, 1373

      create_chainbody :bronze, 11, 1103
      create_chainbody :iron, 26, 1101
      create_chainbody :steel, 41, 1105
      create_chainbody :mithril, 61, 1109
      create_chainbody :adamant, 81, 1111
      create_chainbody :rune, 96, 1113

      create_kiteshield :bronze, 12, 1189
      create_kiteshield :iron, 27, 1191
      create_kiteshield :steel, 42, 1193
      create_kiteshield :mithril, 62, 1197
      create_kiteshield :adamant, 82, 1199
      create_kiteshield :rune, 97, 1201

      create_claws :bronze, 13, 3095
      create_claws :iron, 28, 3096
      create_claws :steel, 43, 3097
      create_claws :mithril, 63, 3099
      create_claws :adamant, 83, 3100
      create_claws :rune, 93, 3101

      create_2h_sword :bronze, 14, 1307
      create_2h_sword :iron, 29, 1309
      create_2h_sword :steel, 44, 1311
      create_2h_sword :mithril, 64, 1315
      create_2h_sword :adamant, 84, 1317
      create_2h_sword :rune, 99, 1319

      create_plateskirt :bronze, 16, 1087
      create_plateskirt :iron, 31, 1081
      create_plateskirt :steel, 46, 1083
      create_plateskirt :mithril, 66, 1085
      create_plateskirt :adamant, 86, 1091
      create_plateskirt :rune, 99, 1093

      create_platelegs :bronze, 16, 1075
      create_platelegs :iron, 31, 1067
      create_platelegs :steel, 46, 1069
      create_platelegs :mithril, 66, 1071
      create_platelegs :adamant, 86, 1073
      create_platelegs :rune, 99, 1079

      create_platebody :bronze, 18, 1117
      create_platebody :iron, 33, 1115
      create_platebody :steel, 48, 1119
      create_platebody :mithril, 68, 1121
      create_platebody :adamant, 88, 1123
      create_platebody :rune, 99, 1127
    end

  end

  class Product
    attr_reader :bar, :type, :lvl_req, :item_id
    def initialize(bar, type, lvl_req, item_id)
      @bar = bar
      @type = type
      @lvl_req = lvl_req
      @item_id = item_id
    end

    def check_reqs(player, first=false)
      if player.get_skill_set.get_current_level(Skill::SMITHING) < @lvl_req
        player.send_message "You need a Smithing level of #{@lvl_req} to make that."
        return false
      end
      if (player.get_inventory.get_amount(@bar.bar_id) < @type.bars)
        if !first
          player.send_message 'You don\'t have enough bars to make that many.'
        else
          player.send_message 'You do not have the number of bars required to make any of those.'
        end
        return false
      end
      if !player.get_inventory.contains(HAMMER)
        player.send_message 'You need a hammer to make anything.'
        return false
      end
      true
    end

    def smith(player)
      player.get_inventory.remove Item.new(@bar.bar_id, @type.bars)
      player.get_inventory.add Item.new(@item_id, @type.amount)
      player.play_animation SMITHING_ANIMATION
      player.get_skill_set.add_experience(Skill::SMITHING, @bar.smith_xp * @type.bars)
      #player.send_message hmmmm
    end

  end

end