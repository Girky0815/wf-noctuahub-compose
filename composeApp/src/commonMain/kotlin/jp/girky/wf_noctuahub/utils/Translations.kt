package jp.girky.wf_noctuahub.utils

object Translations {
  // ミッションの日本語訳
  val missionTypes = mapOf(
    "Assassination" to "抹殺",
    "Assault" to "突撃",
    "Capture" to "確保",
    "Defection" to "脱出",
    "Hive" to "駆除",
    "Defense" to "防衛",
    "Disruption" to "分裂",
    "Excavation" to "発掘",
    "Extermination" to "掃滅",
    "Hijack" to "ハイジャック",
    "Infested Salvage" to "感染回収",
    "Interception" to "傍受",
    "Mobile Defense" to "機動防衛",
    "Rescue" to "救出",
    "Rush" to "ラッシュ",
    "Sabotage" to "妨害",
    "Spy" to "潜入",
    "Survival" to "耐久",
    "Archwing" to "アークウイング",
    "Arena" to "アリーナ",
    "Skirmish" to "小戦",
    "Volatile" to "揮発",
    "Orphix" to "オルフィクス",
    "Bounty" to "依頼",
    "Free Roam" to "自由行動",
    "The Circuit" to "サーキット",
    "Lone Story" to "ローン・ストーリー",
    "The Duviri Experience" to "デュヴィリ・エクスペリエンス",
    "Alchemy" to "錬金術",
    "Netracells" to "ネットセル",
    "Deep Archimedea" to "深延アルキメデア",
    "Mirror Defense" to "ミラー防衛",
    "Conjunction Survival" to "結合耐久",
    "Void Flood" to "フラッド",
    "Corruption" to "フラッド", // 亀裂用
    "Void Cascade" to "カスケード",
    "Void Armageddon" to "アルマゲドン"
  )

  // リソースアイテムの日本語訳
  val resourceTypes = mapOf(
    "Marks of Valiance" to "剛勇の証",
    "Nitain Extract" to "ニタン抽出物",
    "Orokin Catalyst" to "オロキンカタリスト",
    "Orokin Reactor" to "オロキンリアクター",
    "Exilus Adapter" to "エクシラスアダプター",
    "Forma" to "フォーマ",
    "Detonite Injector" to "デトナイトインジェクター",
    "Fieldron" to "フィールドロン",
    "Mutagen Mass" to "ミュータジェンマス",
    "Alad V Nav Coordinate" to "Alad V ナビ座標",
    "Synthula" to "シンスラ",
    "Kavat Genetic Code" to "キャバット遺伝子コード",
    "Void Traces" to "Void トレース",
    "Kuva" to "クバ",
    "Endo" to "Endo",
    "Credits" to "クレジット",
    "Neuroptics" to "ニューロティック",
    "Chassis" to "シャーシ",
    "Blueprint" to "設計図",
    "Systems Component" to "システム",
    "Systems" to "システム",
    "AscensionEventResourceItem" to "ボラタイル・モート"
  )

  // 敵勢力の日本語訳
  val factionTypes = mapOf(
    "Orokin" to "オロキン",
    "Grineer" to "グリニア",
    "Corpus" to "コーパス",
    "Infested" to "感染体",
    "Narmer" to "ナルメル",
    "Sentient" to "センティエント",
    "Corrupted" to "コラプト",
    "Infestation" to "感染体",
    "The Murmur" to "ササヤキ",
    "Scaldra" to "スカルドラ",
    "Techrot" to "テックロット",
    "Anarch" to "アナーク"
  )

  // ExportRegions の factionIndex のマッピング
  val factionIndices = mapOf(
    0 to "Grineer",
    1 to "Corpus",
    2 to "Infested",
    3 to "Corrupted",
    4 to "Orokin",
    7 to "The Murmur",
    8 to "Scaldra",
    9 to "Techrot",
    10 to "Anarch"
  )

  // 惑星の日本語訳
  val planetNames = mapOf(
    "Mercury" to "水星",
    "Venus" to "金星",
    "Earth" to "地球",
    "Lua" to "ルア",
    "Mars" to "火星",
    "Phobos" to "フォボス",
    "Ceres" to "ケレス",
    "Jupiter" to "木星",
    "Europa" to "エウロパ",
    "Saturn" to "土星",
    "Uranus" to "天王星",
    "Neptune" to "海王星",
    "Pluto" to "冥王星",
    "Sedna" to "セドナ",
    "Eris" to "エリス",
    "Void" to "Void",
    "Kuva Fortress" to "クバ要塞",
    "Deimos" to "ダイモス",
    "Zariman" to "Zariman",
    "Veil" to "ヴェール"
  )

  // ボス名の日本語訳
  val bossNames = mapOf(
    "Tyl Regor" to "Tyl Regor",
    "Hyena Pack" to "Hyena Pack",
    "Ambulas" to "Ambulas",
    "Kela De Thaym" to "Kela De Thaym",
    "Ropalolyst" to "ロパロリスト",
    "Phorid" to "Phorid",
    "Lephantis" to "Lephantis",
    "Vay Hek" to "Vay Hek",
    "SORTIE_BOSS_NIRA" to "アルコン Nira",
    "SORTIE_BOSS_AMAR" to "アルコン Amar",
    "SORTIE_BOSS_BOREAL" to "アルコン Boreal"
  )

  // 侵略ミッション種別
  val invasionDescriptions = mapOf(
    "GrineerInvasionGeneric" to "グリニア 侵攻",
    "CorpusInvasionGeneric" to "コーパス 進撃",
    "InfestedInvasionGeneric" to "感染拡大",
    "InfestedInvasionBoss" to "Phorid 出現",
    "Grineer Offensive" to "グリニア 侵攻",
    "Corpus Siege" to "コーパス 進撃",
    "Infested Outbreak" to "感染拡大",
    "Phorid Manifestation" to "Phorid 出現"
  )

  // レリックのティア
  val relicTiers = mapOf(
    "Lith" to "Lith",
    "Meso" to "Meso",
    "Neo" to "Neo",
    "Axi" to "Axi",
    "Requiem" to "Requiem",
    "Omnia" to "オムニア"
  )

  fun translateMissionType(type: String): String = missionTypes[type] ?: type

  // 内部ミッションタイプ（MT_XXX）とのマッピング
  val internalMissionTypes = mapOf(
    "MT_ARENA" to "アリーナ",
    "MT_ARTIFACT" to "分裂",
    "MT_ASSAULT" to "突撃",
    "MT_ASSASSINATION" to "抹殺",
    "MT_CAPTURE" to "確保",
    "MT_CORRUPTION" to "Void Flood",
    "MT_DEFENSE" to "防衛",
    "MT_DISRUPTION" to "分裂",
    "MT_EVACUATION" to "脱出",
    "MT_EXCAVATE" to "発掘",
    "MT_EXTERMINATION" to "掃滅",
    "MT_HIVE" to "駆除",
    "MT_INTEL" to "潜入",
    "MT_LANDSCAPE" to "自由行動",
    "MT_MOBILE_DEFENSE" to "機動防衛",
    "MT_INFESTED_SALVAGE" to "感染回収",
    "MT_PVP" to "コンクレーブ",
    "MT_RESCUE" to "救出",
    "MT_RETRIEVAL" to "ハイジャック",
    "MT_SABOTAGE" to "妨害",
    "MT_SECTOR" to "Dark Sector",
    "MT_SURVIVAL" to "耐久",
    "MT_TERRITORY" to "傍受",
    "MT_VOID_CASCADE" to "カスケード",
    "MT_SHRINE_DEFENSE" to "祭壇防衛",
    "MT_ASCENSION" to "昇天",
    "MT_ALCHEMY" to "錬金術",
    "MT_ENDLESS_CAPTURE" to "レガサイト収穫"
  )

  fun translateInternalMissionType(raw: String): String {
    val normalized = raw.replace("DT_", "MT_")
    internalMissionTypes[normalized]?.let { return it }
    return translateMissionType(raw)
  }

  // ----- ディセンディア（1999）専用 ミッション・ミニミッション辞書 -----
  // 通常のミッション(DT_ASSASSINATION 等)とは異なる独自のミニミッション名や
  // ディセンディア特有の目標がAPIから返ってきた場合は、ここに追加してください。
  // 例: "HackTerminals" to "端末ハッキング", "RescueTarget" to "ターゲット救出"
  val descendiaMiniMissions = mapOf(
    //"HackTerminals" to "端末ハッキング" // 仮の例です。ゲーム内の表記に合わせて書き換えてください。
    "DT_PRESURE_GAUGE" to "圧力鍋 (錬金術: 圧力制御フェーズ)",
    "DT_SABOTAGE_HIVE" to "ハイブ",
    "DT_PROTOFRAME" to "チェックポイント",
    "DT_EXCAVATION" to "発掘",
    "DT_EXTERMINATE" to "掃滅",
    "DT_INTERCEPTION" to "機動傍受",
    "DT_MIMICS" to "略奪ルーレット",
    "DT_LOOT" to "略奪 (ボーナス部屋)",
    "DT_BREAK_TARGETS" to "ホログローブ破壊",
    "DT_BOSS" to "ボス戦",
    "DT_UNIQUE" to "時の難題(レース)",
    "DT_NETRACELLS" to "標的排除(ネクロマイトドローン)",
    "DT_COLLECTION" to "バイトブラスト収集",
    "DT_SABOTAGE_DEFENSE" to "防衛", // 時のゆりかご
    "DT_LOOT_CREATURES" to "グラズリング排除",
  )

  /**
   * ディセンディア用ミッションタイプの翻訳関数。
   * 1. 最初に descendiaMiniMissions に完全一致するか探す
   * 2. なければ通常のミッション（MT_ / DT_ 変換）として探す
   */
  fun translateDescendiaMissionType(raw: String): String {
    descendiaMiniMissions[raw]?.let { return it }
    return translateInternalMissionType(raw)
  }

  // ディセンディア（1999）用 モディファイア（リスク）辞書
  val descendiaModifiers = mapOf(
    "RangedArcadiaOnly" to "バブルブラスター",
    "VeryToxic" to "猛毒",
    "Darkness" to "ソル追放",
    "LowEnergy" to "低エネルギー",
    "HighDamage" to "被ダメージ増加",
    "FireAndIce" to "エクシマス・カバル(炎と氷)",
    "Escapist" to "静かなる撤退",
    "JadeGuardian" to "ジェイドガーディアン",
    "NC_SpikeCeiling" to "落下する破片",
    "SpikeCeiling" to "落下する破片",
    "PoisonGas" to "化学兵器",
    "Wisp" to "Marie のサンクチュアリ",
    "Harrow" to "Lyon のサンクチュアリ",
    // "Devil" to "Roathe の苦悩",
    "BasicMimics" to "",
    "BasicLoot" to "",
    "FieryTrail" to "炎の軌跡",
    "UnseenFoes" to "隠れた脅威",
    "GiantRealm" to "巨人化",
    "NC_SlipAndSlide" to "摩擦なし",
    // "ArbitersNightmareLawyer" to "仲裁ドローン", 
    "ArbitrationDrones" to "仲裁ドローン",
    "HeadShotsOnly" to "弱点のみにダメージ可能(=封鎖装甲)",
    "SecuritySpin" to "レーザーリンボ",
    "BasicLootCreatures" to "",
    "RaceHorse" to "バトルケイス",
    "HorseCombotOnly" to "バトルケイス",
    
    // エクシマス・カバル
    "BlitzLeech" to "エクシマス・カバル: リーチブリッツ",

    // ボス
    "HyenaPack" to "Hyenaパック",
    "InfestedBoyBand" to "テクノサイト・コーダ",

  )

  fun translateDescendiaModifier(name: String): String {
    descendiaModifiers.forEach { (key, value) ->
      if (name.contains(key, ignoreCase = true)) {
        return value
      }
    }
    return name
  }



  fun translateResource(type: String): String {
    resourceTypes[type]?.let { return it }

    var translated = type
    translated = translated.replace(Regex(" Blueprint$"), " 設計図")
    translated = translated.replace(Regex(" Chassis$"), " シャーシ")
    translated = translated.replace(Regex(" Neuroptics$"), " ニューロティック")
    translated = translated.replace(Regex(" Systems Component$"), " システム")
    translated = translated.replace(Regex(" Systems$"), " システム")
    translated = translated.replace(Regex("Star Days"), "星の日")
    translated = translated.replace(Regex(" Glyph$"), " グリフ")
    translated = translated.replace(Regex("^Avatar Image "), "")

    return translated
  }

  fun translateFaction(faction: String, node: String? = null): String {
    val normalizedFaction = faction.lowercase().replaceFirstChar { it.uppercase() }
    if (node != null) {
      if (normalizedFaction == "Corpus" && node.contains("Jupiter")) {
        return "コーパスアマルガム"
      }
      if (normalizedFaction == "Grineer" && node.contains("Kuva Fortress")) {
        return "クバグリニア"
      }
    }
    return factionTypes[normalizedFaction] ?: factionTypes[faction] ?: faction
  }

  fun translateFactionIndex(index: Int?): String {
    return translateFaction(factionIndices[index] ?: return "不明")
  }

  fun translateNode(node: String?): String {
    if (node.isNullOrBlank()) return ""
    val match = Regex("""^(.+)\s\((.+)\)$""").find(node)
    if (match != null) {
      val name = match.groupValues[1]
      val planet = match.groupValues[2]
      val translatedPlanet = planetNames[planet] ?: planet
      return "$name ($translatedPlanet)"
    }
    return node
  }

  fun translateInvasionDesc(desc: String): String = invasionDescriptions[desc] ?: desc

  fun translateTier(tier: String): String = relicTiers[tier] ?: tier

  fun translateBoss(boss: String): String = bossNames[boss] ?: boss

  // ------ 出撃(ソーティ)関連の翻訳 ------
  val sortieModifiers = mapOf(
    "Extreme Cold" to "極寒",
    "Dense Fog" to "濃霧",
    "Cryogenic Leakage" to "極低温漏出",
    "Fire" to "火災",
    "Electromagnetic Anomalies" to "電磁変異",
    "Radiation Hazard" to "放射線障害",
    "Radiation Pockets" to "放射線障害",
    "Low Gravity" to "低重力",
    "Energy Reduction" to "エネルギー減少",
    "Slash" to "切断",
    "Impact" to "衝撃",
    "Puncture" to "貫通",
    "Heat" to "火炎",
    "Cold" to "冷気",
    "Electricity" to "電気",
    "Toxin" to "毒",
    "Blast" to "爆発",
    "Corrosive" to "腐食",
    "Gas" to "ガス",
    "Magnetic" to "磁気",
    "Radiation" to "放射線",
    "Viral" to "感染",
    "Augmented Enemy Armor" to "敵アーマー増強",
    "Enhanced Enemy Shields" to "敵シールド増強",
    "Eximus Stronghold" to "エクシマスの要塞",
    "Sniper Only" to "精密ライフルのみ",
    "Assault Rifle Only" to "アサルトライフルのみ",
    "Melee Only" to "近接のみ",
    "Bow Only" to "弓のみ",
    "Pistol Only" to "ピストルのみ",
    "Shotgun Only" to "ショットガンのみ",
    "Weapon Restriction" to "武器制限"
  )

  fun translateSortieModifier(modifier: String): String {
    sortieModifiers[modifier]?.let { return it }

    if (modifier.startsWith("Environmental Effect: ")) return modifier.substring("Environmental Effect: ".length)
    if (modifier.startsWith("Environmental Hazard: ")) return modifier.substring("Environmental Hazard: ".length)
    if (modifier.startsWith("Enemy Physical Enhancement: ")) return "敵物理強化 (${modifier.substring("Enemy Physical Enhancement: ".length)})"
    if (modifier.startsWith("Enemy Elemental Enhancement: ")) return "敵属性強化 (${modifier.substring("Enemy Elemental Enhancement: ".length)})"
    if (modifier.startsWith("Weapon Restriction: ")) return "武器制限: ${modifier.substring("Weapon Restriction: ".length)}"

    return modifier
  }

  // ------ アルキメデア関連の翻訳 ------
  fun translateArchimedeaType(typeKey: String): String {
    if (typeKey.contains("C T_ L A B")) return "深淵アルキメデア"
    if (typeKey.contains("C T_ H E X")) return "次元アルキメデア"
    return typeKey
  }

  fun translateArchimedeaFaction(faction: String): String {
    if (faction == "Man in the Wall") return "ササヤキ"
    return factionTypes[faction] ?: faction
  }

  fun translateArchimedeaMission(missionType: String, archimedeaType: String): String {
    if (archimedeaType.contains("C T_ L A B")) {
      if (missionType == "Defense") return "ミラー防衛"
    }
    if (archimedeaType.contains("C T_ H E X")) {
      if (missionType == "Defense") return "ステージ防衛"
      if (missionType == "Survival") return "ヘルスクラバー 耐久"
      if (missionType == "Legacyte Harvest") return "レガサイト収穫"
    }
    return missionTypes[missionType] ?: missionType
  }

  // 深淵 / 次元アルキメデアの各種モディファイア用辞書（主要なもののみ抜粋・必要に応じ拡張）
  // nameプロパティのみをマッピング
  val archimedeaModifiers = mapOf(
    "Necramech Influx" to "ネクロメカ流入",
    "Fissure Cascade" to "亀裂連鎖",
    "Damage Link" to "損傷リンク",
    "Unpowered Capsules" to "寄生タワー",
    "Parasitic Towers" to "寄生タワー",
    "Hostile Support" to "敵対的支援",
    "Hazardous Area" to "危険エリア",
    "Hazardous Wares" to "危険物資",
    "Alchemical Shields" to "無敵の錬金術",
    "Eximus Grenadiers" to "エクシマスアンフォール",
    "Eroding Senses" to "感覚麻痺",
    "Glyph Inflation" to "グリフインフレーション",
    "Glyph Trap" to "グリフトラップ",
    "Radioactive Decay" to "放射性衰弱",
    "Barbed Glyphs" to "バーブグリフ",
    "Coordinated Front" to "戦力配備",
    "Relentless Tide" to "容赦ない潮流",
    "Angelic Company" to "天使の仲間",
    "Fragment Two" to "フラグメント・ツー",
    "Engorged Gruzzlings" to "充血グラズリング",
    "Unified Purpose" to "統一目的",
    "Double Trouble" to "ダブルデモリッシャー",
    "Hostile Regeneration" to "敵性再生",
    "Vampyric Liminus" to "バンパイヤ・リミナス",
    "Adaptive Resistance" to "適応抵抗",
    "Bolstered Belligerents" to "戦闘員の増強",
    "Shootout" to "射撃戦",
    "Melee" to "接近戦",
    "Fortified Foes" to "要塞化",
    "Myopic Munitions" to "近視戦闘",
    "Postmortal Surges" to "死後の呪い",
    "Elemental Enhancement" to "属性強化",
    "Eximus Reinforcement" to "エクシマスの増援",
    "Bold Venture" to "乾坤一擲(けんこんいってき)",
    "Devil's Bargain" to "悪魔の取引",
    "Entanglement" to "絡み合い",
    "Commanding Culverins" to "指揮機カルヴァリン",
    "Explosive Potential" to "爆発的可能性",
    "Alluring Arcocanids" to "いざなうアルコカニド",
    "Sealed Armor" to "封鎖装甲", // CT_LAB用
    "Powerless" to "アビリティ封印",
    "Gear Embargo" to "ギア制限",
    "Secondary Wounds" to "二次創傷",
    "Lethargic Shields" to "不活性シールド",
    "Ammo Deficit" to "弾薬不足(Deficit)",
    "Fractured Armor" to "ひび割れ",
    "Untreatable" to "治療不能",
    "Abbreviated Abilities" to "アビリティ短縮",
    "Swooning" to "失神消耗",
    "Transference Distortion" to "転移障害",
    "Framecurse syndrome" to "フレームカース・シンドローム",
    "Knifestep Syndrome" to "ナイフステップ・シンドローム",
    "Exhaustion" to "エネルギー枯渇",
    "Energy Exhaustion" to "エネルギー枯渇",
    "Ammo Scarcity" to "弾薬不足(Scarcity)",
    "Exposure Curse" to "摘発の呪い",
    "Hematic Syndrome" to "血紅症候群",
    "Vampiric Syndrome" to "吸血鬼症候群",
    "Void Energy Overload" to "アビリティ オーバーロード",
    "Ability Overload" to "アビリティ オーバーロード",
    "Undersupplied" to "供給不足",
    "Energy Starved" to "狭窄",
    "Hypersensitive" to "過敏症",
    "Dull Blades" to "なまくら",
    "Permanent Injury" to "後遺症",
    "Anti Guard" to "油断"
  )

  fun translateArchimedeaModifierName(name: String, typeKey: String? = null): String {
    if (name == "Sealed Armor") {
      if (typeKey != null && typeKey.contains("C T_ H E X")) {
        return "封印された鎧"
      }
      return "封鎖装甲"
    }
    return archimedeaModifiers[name] ?: name
  }

  // ------ 侵略ミッション関連の翻訳 ------
  val invasionWeapons = mapOf(
    "SnipetronVandal" to "Snipetron Vandal",
    "TwinVipersWraith" to "Twin Vipers Wraith",
    "DeraVandal" to "Dera Vandal",
    "KarakWraith" to "Karak Wraith",
    "StrunWraith" to "Strun Wraith",
    "LatronWraith" to "Latron Wraith",
    "Sheev" to "Sheev",
    "GrineerCombatKnifeSortie" to "Sheev",
    "GrineerCombatKnife" to "Sheev",
    "GorgonWraith" to "Gorgon Wraith",
    "IgnisWraith" to "Ignis Wraith"
  )

  val invasionParts = mapOf(
    "Receiver" to "レシーバー",
    "Barrel" to "バレル",
    "Stock" to "ストック",
    "Blueprint" to "設計図",
    "Link" to "リンク",
    "Pouch" to "ポーチ",
    "Stars" to "スター",
    "Blade" to "ブレード",
    "Hilt" to "ヒルト",
    "Grip" to "グリップ",
    "String" to "ストリング",
    "UpperLimb" to "アッパーリム",
    "LowerLimb" to "ロアーリム",
    "Guard" to "ガード",
    "Heatsink" to "ヒートシンク"
  )

  val invasionMaterials = mapOf(
    "ChemComponent" to "デトナイトインジェクター",
    "EnergyComponent" to "フィールドロン",
    "BioComponent" to "ミュータジェンマス",
    "InfestedAladCoordinate" to "Mutalist Alad V ナビ座標",
    "WeaponUtilityUnlocker" to "武器エクシラスアダプター",
    "UtilityUnlocker" to "Warframe エクシラスアダプター"
  )

  /**
   * 侵略ミッションの報酬アイテム名を翻訳する
   * 武器名＋パーツ名（例: SnipetronVandalReceiver）などを分割して翻訳
   */
  fun translateInvasionReward(rewardName: String): String {
    // まず汎用素材等で完全一致するか確認
    invasionMaterials[rewardName]?.let { return it }

    var translated = rewardName
    var matched = false

    // 汎用素材等の前方一致置換（例: UtilityUnlockerBlueprint -> Warframe エクシラスアダプターBlueprint）
    invasionMaterials.forEach { (key, value) ->
      if (translated.startsWith(key)) {
        translated = translated.replaceFirst(key, value)
        matched = true
      }
    }

    // 武器名の置換
    if (!matched) {
      invasionWeapons.forEach { (key, value) ->
        if (translated.startsWith(key)) {
          translated = translated.replaceFirst(key, "$value ")
          matched = true
        }
      }
    }

    // パーツ名の置換
    invasionParts.forEach { (key, value) ->
      if (translated.endsWith(key)) {
        translated = translated.removeSuffix(key) + value
      }
    }

    // 通常のリソース名でも一応変換試行
    if (!matched) {
      translated = translateResource(translated.trim())
    }

    return translated.trim()
  }

  // ------ Nightwave 関連の翻訳 ------
  val nightwaveChallenges = mapOf(
    // デイリー 属性キル系
    "SeasonDailyKillEnemiesWithViral" to Pair("バズれ", "敵を150体感染属性で倒す"),
    "SeasonDailyKillEnemiesWithCorrosive" to Pair("メルトダウン", "敵を150体腐食属性で倒す"),
    "SeasonDailyKillEnemiesWithBlast" to Pair("爆弾魔", "敵を150体爆発属性で倒す"),
    "SeasonDailyKillEnemiesWithCold" to Pair("急速冷凍", "敵を150体冷気属性で倒す"),
    "SeasonDailyKillEnemiesWithElectric" to Pair("回路のショート", "敵を150体電気属性で倒す"),
    "SeasonDailyKillEnemiesWithFire" to Pair("放火魔", "敵を150体火炎属性で倒す"),
    "SeasonDailyKillEnemiesWithToxin" to Pair("毒殺者", "敵を150体毒属性で倒す"),
    "SeasonDailyKillEnemiesWithMagnetic" to Pair("引き寄せる力", "敵を150体磁気属性で倒す"),
    "SeasonDailyKillEnemiesWithRadiation" to Pair("原子炉", "敵を150体放射線属性で倒す"),

    // デイリー
    "SeasonDailyAimGlide" to Pair("グライダー", "エイムグライドで敵を15体倒す"),
    "SeasonDailyKillEnemiesWithFinishers" to Pair("フィニッシャーによる撃破", "フィニッシャーで敵を10体倒す"),
    "SeasonDailyKillEnemiesWithHeadshots" to Pair("マークスマン", "ヘッドショットで敵を40体倒す"),
    "SeasonDailyKillEnemiesWhileOnKDrive" to Pair("スリルライダー", "K-ドライブ、ケイス、ベロシポッド、アトミサイクルに乗った状態で敵を20体倒す"),

    // ウィークリー (末尾の数字は関数側で動的に抽出するためキーからは除外)
    "SeasonWeeklyPermanentCompleteMissions" to Pair("ミッション完了", "ミッションを15回完了する"),
    "SeasonWeeklyPermanentKillEximus" to Pair("エクシマス駆逐者", "エクシマスを30体倒す"),
    "SeasonWeeklyPermanentKillEnemies" to Pair("これは警告ではない", "敵を500体倒す"),

    "SeasonWeeklyCompleteClemMission" to Pair("良き友", "Clemの週ミッションを完了する"),
    "SeasonWeeklyBoardingPartyNoDamage" to Pair("パーフェクト", "レールジャックに突撃した敵をノーダメージで撃破する"),
    "SeasonWeeklyHardKillEnemiesSteelPath" to Pair("冷徹", "鋼の道のりミッションで敵を1,000体倒す"),
    "SeasonWeeklyHardFriendsDefense" to Pair("防衛", "防衛ミッションを最低12ウェーブまで進めてクリアする")
  )

  fun translateNightwaveChallenge(challenge: String): Pair<String, String> {
    val key = challenge.substringAfterLast("/")
    
    // 末尾の数字を抽出
    val matchResult = Regex("""^(SeasonWeeklyPermanent[A-Za-z]+)(\d+)$""").matchEntire(key)
    if (matchResult != null) {
      val baseKey = matchResult.groupValues[1]
      val weekNum = matchResult.groupValues[2]
      val translation = nightwaveChallenges[baseKey]
      if (translation != null) {
        // 課題名の後ろに「 週数」を付与して表示
        return Pair("${translation.first} $weekNum", translation.second)
      }
    }

    return nightwaveChallenges[key] ?: Pair(key, "")
  }

  // ------ 1999 Calendar 関連の翻訳 ------
  val calendarEventTypes = mapOf(
    "CET_CHALLENGE" to "課題",
    "CET_REWARD" to "報酬",
    "CET_UPGRADE" to "バフ"
  )

  val calendarSeasons = mapOf(
    "CST_SPRING" to "春",
    "CST_SUMMER" to "夏",
    "CST_AUTUMN" to "秋",
    "CST_WINTER" to "冬"
  )

  fun translateCalendarType(type: String): String = calendarEventTypes[type] ?: type
  fun translateCalendarSeason(season: String): String = calendarSeasons[season] ?: season

  // ------ サーキット関連の翻訳 ------
  val circuitCategories = mapOf(
    "EXC_NORMAL" to "通常",
    "EXC_HARD" to "鋼の道のり"
  )

  fun translateCircuitCategory(category: String): String = circuitCategories[category] ?: category

  // ------ イベント関連の翻訳 ------
  val eventNames = mapOf(
    "JadeShadows" to "獣の巣窟作戦",
    "JadeShadowsEventName" to "獣の巣窟作戦",
    "BellyOfBeast" to "アトラメンタム作戦",
    "Belly" to "アトラメンタム作戦",
    "Ghoul" to "グール粛清",
    "GhoulPurge" to "グール粛清",
    "HeatFissures" to "サーミアの裂け目",
    "HeatFissuresEventName" to "サーミアの裂け目",
    "Razorback" to "Razorback Armada",
    "RazorbackArmada" to "Razorback Armada",
    "Fomorian" to "バロール・フォーモリアン",
    "BalorFomorian" to "バロール・フォーモリアン"
  )

  fun translateEvent(eventDescOrTag: String): String {
    val key = eventDescOrTag.substringAfterLast("/")
    eventNames[key]?.let { return it }

    // 部分一致のチェック
    val lower = eventDescOrTag.lowercase()
    if (lower.contains("jadeshadows")) return "獣の巣窟作戦"
    if (lower.contains("belly")) return "アトラメンタム作戦"
    if (lower.contains("ghoul")) return "グール粛清"
    if (lower.contains("heatfissures")) return "サーミアの裂け目"
    if (lower.contains("razorback")) return "Razorback Armada"
    if (lower.contains("fomorian")) return "バロール・フォーモリアン"

    return eventDescOrTag
  }
}

