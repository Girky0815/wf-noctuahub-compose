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
  "OrokinCatalystBlueprint" to "オロキンカタリスト設計図",
  "OrokinCatalyst" to "オロキンカタリスト",
  "OrokinReactor" to "オロキンリアクター",
  "OrokinReactorBlueprint" to "オロキンリアクター設計図",
  "ExilusAdapterBlueprint" to "エクシラスアダプター",
  "Forma" to "フォーマ",
  "Detonite Injector" to "デトナイトインジェクター",
  "Fieldron" to "フィールドロン",
  "MutagenMass" to "ミュータジェンマス",
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
  "AscensionEventResourceItem" to "ボラタイル・モート",
  "GuardianTitle" to "ホノリア",
  "ArchonCrystalNiraMythic" to "Tauforged アルコンの欠片「琥珀」",
  "ArchonCrystalAmarMythic" to "Tauforged アルコンの欠片「真紅」",
  "ArchonCrystalBorealMythic" to "Tauforged アルコンの欠片「蒼天」"
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
  "DT_UNIQUE" to "バトルケイス",
  "DT_RACE" to "時の難題(レース)",
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
  "SlipAndSlide" to "摩擦なし",
  // "ArbitersNightmareLawyer" to "仲裁ドローン", 
  "ArbitrationDrones" to "仲裁ドローン",
  "HeadShotsOnly" to "弱点のみにダメージ可能(=封鎖装甲)",
  "SecuritySpin" to "レーザーリンボ",
  "BasicLootCreatures" to "",
  "RaceHorse" to "",
  "HorseCombatOnly" to "",
  "CoHHorseAura" to "強制騎乗状態(下馬不可)",
  // "DisableSelfReviveAura" to "一切の蘇生不可",
  "HorseCombotOnly" to "",
  "VoidAberration" to "バンパイヤ・リミナス",
  "GenericFortifiedFoesWeakpointAura" to "弱点ハイライト",
  "HordeWeakPoints" to "弱点いっぱい",
  "GrenadesOnly" to "アンフォールに弱い敵",
  "GrenadeSpawnAura" to "アンフォールスポーン",
  "BasicRace" to "通常",
  "NC_MineField" to "地雷原",
  "Manics" to "マニック・マニア",

  
  // エクシマス・カバル
  "BlitzLeech" to "エクシマス・カバル: リーチブリッツ",
  "ToxicLeech" to "エクシマス・カバル: トキシックリーチ",
  "FireAndIce" to "エクシマス・カバル: 火と氷",
  "ToxicFire" to "エクシマス・カバル: トキシックフレイム",
  "FreezeInShoot" to "エクシマス・カバル: フローズンライト",
  "ShockingLeech" to "エクシマス・カバル: ショッキングリーチ",

  // ボス
  "HyenaPack" to "Hyenaパック",
  "InfestedBoyBand" to "テクノサイト・コーダ",
  "Raptor2" to "Raptors",

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
  "SeasonDailyKillEnemiesWithGas" to Pair("バイオハザード", "敵を150体ガス属性で倒す"),

  // デイリー
  "SeasonDailyKillEnemies" to Pair("これは警告だ", "敵を200体倒す"),
  "SeasonDailyAimGlide" to Pair("グライダー", "エイムグライドで敵を15体倒す"),
  "SeasonDailyKillEnemiesWithFinishers" to Pair("処刑者", "フィニッシャーで敵を10体倒す"),
  "SeasonDailyKillEnemiesWithHeadshots" to Pair("マークスマン", "ヘッドショットで敵を40体倒す"),
  "SeasonDailyKillEnemiesWhileOnKDrive" to Pair("スリルライダー", "K-ドライブ、ケイス、ベロシポッド、アトミサイクルに乗った状態で敵を20体倒す"),
  "SeasonDailyKillEnemiesWithSecondary" to Pair("小は大を征す", "セカンダリ武器で敵を150体倒す"),
  "SeasonDailyMercyKill" to Pair("慈悲なし", "Mercy で敵を1体倒す"),
  "SeasonDailyPlayEmote" to Pair("表現豊か", "ギアメニューからエモートを1回行う"),
  "SeasonDailyCollectCredits" to Pair("貯蓄家", "合計 15,000 Cr を拾う"),
  "SeasonDailyTwoForOne" to Pair("一石二鳥", "1回の弓矢での攻撃で2体以上の敵を倒す"),

  "SeasonDailyVisitFeaturedDojo" to Pair("旅行目的", "特選Dojoを訪れる"),
  "SeasonDailyDeployAirSupport" to Pair("風通し良好", "エアリアルサポートチャージを展開する"),
  "SeasonDailyCompleteMissionPrimary" to Pair("手一杯", "プライマリ武器のみを装備してミッションをクリアする"),
  "SeasonDailyCompleteMissionSecondary" to Pair("サイドアーム", "セカンダリ武器のみを装備してミッションをクリアする"),
  "SeasonDailyCompleteMissionMelee" to Pair("剣客", "近接武器のみを装備してミッションをクリアする"),
  "SeasonDailyBulletJump" to Pair("トランポリン", "バレットジャンプを150回行う"),
  "SeasonDailyCompleteMission" to Pair("エージェント", "ミッションを1回クリアする"),
  "SeasonDailyDeployGlyph" to Pair("グラフィティ", "ギアメニューに「グリフプリズム」を装備し、ミッション中に使用してグリフを展開する"),
  "SeasonDailyKillEnemiesWithAbilities" to Pair("力の誇示", "アビリティで敵を150体倒す"),
  


  // ウィークリー (末尾の数字は関数側で動的に抽出するためキーからは除外)
  "SeasonWeeklyPermanentCompleteMissions" to Pair("ミッション完了", "ミッションを15回完了する"),
  "SeasonWeeklyPermanentKillEximus" to Pair("エクシマス駆逐者", "エクシマスを30体倒す"),
  "SeasonWeeklyPermanentKillEnemies" to Pair("これは警告ではない", "敵を500体倒す"),
  "SeasonWeeklyLoyalty" to Pair("忠誠心", "任意のシンジケートにて5,000地位を獲得する"),

  "SeasonWeeklyCompleteSpy" to Pair("諜報員", "潜入ミッションを3回クリアする"),

  "SeasonWeeklyCompleteClemMission" to Pair("良き友", "Clemの週ミッションを完了する"),
  "SeasonWeeklyCollector" to Pair("収集者", "デュヴィリで素材を100個集める"),
  "SeasonWeeklyCatchRareVenusFish" to Pair("金星の釣り人", "オーブ峡谷で3匹のレアなサーボ魚を捕まえる"),
  "SeasonWeeklyNightAndDay" to Pair("昼と夜", "カンビオン荒地で Vome または Fass の残余を10個集める"),
  "SeasonWeeklyFinelyTuned" to Pair("調弦済み", "デュヴィリで異なる Shawzin の曲を3曲演奏する"),
  "SeasonWeeklyUnlockDragonVaults" to Pair("お宝収集", "ダイモス・遺跡船タイル(抹殺/防衛除く)でドラゴンキー貯蔵庫を4個解放する"),
  "SeasonWeeklySanctuaryOnslaught" to Pair("テスト被験者", "サンクチュアリ交戦(通常/エリート)を8ゾーンクリアする"),
  "SeasonWeeklyUseForma" to Pair("極性付与", "シミュラクラム以外の場所で任意の武器や Warframe にフォーマを使用して極性を付与する"),
  "SeasonWeeklySolveCiphers" to Pair("ハッカー", "コンソールを10個ハッキングする"),
  "SeasonWeeklyCompleteTreasures" to Pair("生を吹き込む", "ウィークリーアヤタントレジャーハントをクリアする"),
  "SeasonWeeklyKillEnemiesInMech" to Pair("ネクロライザー", "ネクロメカで100体の敵を倒す"),


  // エリートウィークリー
  "SeasonWeeklyEliteCompleteMission" to Pair("完璧な", "ミッションを1回完了する"),
  "SeasonWeeklyBoardingPartyNoDamage" to Pair("パーフェクト", "レールジャックに突撃した敵をノーダメージで撃破する"),
  "SeasonWeeklyHardKillEnemiesSteelPath" to Pair("冷徹", "鋼の道のりミッションで敵を1,000体倒す"),
  "SeasonWeeklyHardFriendsDefense" to Pair("防衛", "防衛ミッションを最低12ウェーブまで進めてクリアする"),
  "SeasonWeeklyHardRailjackMissions" to Pair("エリート探検者", "レールジャックミッションを8回クリアする"),
  "SeasonWeeklyHardThePriceOfFreedom" to Pair("自由の代償", "任意の Granum クラウンを使用して、コーパス船タイルに囚われたソラリス民を1人救出する"),
  "SeasonWeeklyHardCompleteSortie" to Pair("ソーティーエキスパート", "ソーティーを3回クリアする"),
  "SeasonWeeklyHardUnlockRelics" to Pair("レリックの解放", "レリックを10個解放する"),
  "SeasonWeeklyHardVitalArbiter" to Pair("重要仲裁人", "仲裁ミッションを1回クリアする"),
  "SeasonWeeklyHardFastCapture" to Pair("スピードスター", "確保ミッションを90秒以内にクリアする"),
  "SeasonWeeklyHardLuaPuzzles" to Pair("試練", "ルアで試練の間を4個クリアする"),
  "SeasonWeeklyHardFriendsSurvival" to Pair("耐久", "耐久系ミッションを最低20分進めてクリアする"),
  "SeasonWeeklyHardEliteSanctuaryOnslaught" to Pair("エリートテスト被験者", "エリートサンクチュアリ交戦を8ゾーンクリアする"),
  "SeasonWeeklyHardEliteBeastSlayer" to Pair("エリート獣殺し", "鋼の道のり版デュヴィリ(エクスペリエンス or ローン・ストーリー)でオロワームを倒す"),
  "SeasonWeeklyHardTerminated" to Pair("殲滅", "隔離庫のネクロメカを3体倒す"),
  "SeasonWeeklyHardKillEximus" to Pair("エクシマス処刑人(ハード)", "100体のエクシマスを倒す"),
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
  "CST_FALL" to "秋",
  "CST_WINTER" to "冬"
  )

  val calendarUpgrades = mapOf(
    // ヘックスブースト (パッシブ効果)
    "MeleeAttackSpeed" to Pair("容赦なし", "近接攻撃速度+25%。"),
    "MeleeCritChance" to Pair("熟練の精度", "近接クリティカル率+20% (ヘビー攻撃は2倍)。"),
    "Armor" to Pair("分厚い皮膚", "装甲値+250。"),
    "CompanionDamage" to Pair("援助は任せろ", "スペクターとコンパニオンのダメージが+250%増加する。"),
    "PsionicFeedback" to Pair("サイオニックフィードバック", "ダメージ時 被ダメージ時、10%の確率で半径5mの放射線爆発 (250ダメージ) を起こす。"),
    "MagazineCapacity" to Pair("ヘビーマガジン", "マガジンサイズを25%増やす。"),
    "GasChanceToPrimaryAndSecondary" to Pair("毒性弾", "プライマリ武器とセカンダリ武器は25%のガス状態異常確率を追加で得る。"),
    "PunchToPrimary" to Pair("パンチカード", "プライマリ武器の貫通距離を1.5m追加する。"),
    "EnergyRestoration" to Pair("エスプレッソショット", "エネルギー回復 +2EN/s"),
    "RecoveryBoost" to Pair("救急医療", "オーブピックアップがもたらすすべての回復効果が25%増加する。"),
    "EnergyOrbToAbilityRange" to Pair("視野拡大", "エネルギーオーブ回収時10秒間、アビリティ範囲が10%増加する。"),
    "AbilityStrength" to Pair("パワーゲイン", "アビリティ威力+25%。"),
    "BottledLightning" to Pair("瓶詰めの稲妻", "すべての武器に25%の状態異常ダメージと電気状態異常確率を追加する。"),
    "OvershieldCap" to Pair("硬化", "オーバーシールドの上限が50%上昇する(オーバーシールド上限 1,800)。キル時に50シールド回復する。"),
    
    // ヘックスファクター (条件発動効果)
    "ComboKiller" to Pair("コンボキラー", "コンボ倍率は、近接攻撃後に敵がフィニッシャー待機状態になる確率を高める。コンボ倍率ごとに5%増加し、Venka Primeの場合は最大65%まで上昇する。"),
    "HeavyJavelin" to Pair("ヘビージャベリン", "ヘビー近接攻撃は、3m範囲で1000ダメージ（コンボ倍率で変動）与えるRadial Javelinを発動する。"),
    "SliceAndDice" to Pair("スライス＆ダイス", "近接スライディング攻撃は、スライディング攻撃のクリティカル確率を5%、スライディング速度を20%増加させ、スライディング近接攻撃ごとに10秒間スライディング摩擦を10%減少させる。最大10まで重複。"),
    "EnergyWavesOnCombo" to Pair("コンボウェーブ", "コンボ倍率が7倍になると、すべての近接攻撃はエネルギー波を放つ。"),
    "MoreTheMerrier" to Pair("大は小を兼ねる", "20m以内の非テンノの味方全員が、範囲内の味方ごとに近接攻撃速度5%と発射速度20%を得る。"),
    "CompanionsRadiationChance" to Pair("有効なフォールアウト", "スペクターまたはコンパニオンの攻撃は25%の確率で放射線状態異常を引き起こす。"),
    "AtonementOpportunity" to Pair("つぐなう機会", "倒した敵は、状態異常のスタックごとに1%の確率で味方のスペクターとして復活する。スペクターは30秒間活動する。"),
    "CopyAndPaste" to Pair("コピー＆ペースト", "125エネルギーを消費するごとに、現在展開されているスペクターかコンパニオンのスペクトルクローンをランダムに生み出す。"),
    "BlastEveryXShots" to Pair("楽しんでこい", "10発ごとにヒット時爆発スタックが10追加される。"),
    "CumulativeCartridges" to Pair("累積カートリッジ", "リロードするまで、ショットごとに状態異常確率が1%増加する。"),
    "RefundBulletOnStatusProc" to Pair("フリーショット", "状態異常を引き起こすと、10%の確率で起こした状態異常の弾丸をマガジンに充填する。"),
    "TrickShot" to Pair("トリックショット", "弾がヒットする度に、追尾弾を発射する確率が10%増加する。発砲する度に追尾弾確率は5%低下する。"),
    "MagneticMenace" to Pair("磁気の脅威", "アビリティを5回使うたびに前方50m以内の敵は磁気状態異常を受ける。"),
    "SharingIsCaring" to Pair("分け合いは大事", "アビリティを10回発動するたびに、自身と味方が次に発動するアビリティのコストを0にする。"),
    "Overpower" to Pair("圧倒的な力", "アビリティ発動時、アビリティの規定消費エネルギー単位ごとに、5秒間アビリティ威力が2%増加し、アビリティ効率が1%悪化する。上限：アビリティ威力 +150%、効率 -75%。"),
    "ForceOfAttraction" to Pair("引力", "磁気状態異常は、スタック毎1m範囲で周辺の敵を引き寄せる。"),
    "ElectricalDamageOnBulletJump" to Pair("ダイナモジャンプ", "バレットジャンプから着地後の5秒間は電気ダメージが50%増加する。"),
    "StaticAccumulation" to Pair("静電気蓄積", "移動距離1mごとに電気ダメージが付与される。攻撃時に蓄積した静電気の10%が消費される。"),
    "SpeedBuffsWhenAirborne" to Pair("フリークエントフライヤー特典", "空中では、リロード速度、発射速度、発動速度、近接攻撃速度が100%増加する。"),
    "HitAndSplit" to Pair("ヒット＆スプリット", "近接攻撃のクリティカルヒットは、確率で攻撃速度と移動速度を10秒間5%上昇させる。"),
    "TargetedTherapeutics" to Pair("標的治療薬", "ヘルスオーブを撃つと回収できる。回収時にヘルスオーブは25%の確率で分裂する。"),
    "GenerateOmniOrbsOnWeakKill" to Pair("強制輸血", "弱点への攻撃で敵を倒すとユニバーサルオーブが25%の確率で生成される。"),
    "ShieldRefill" to Pair("シールド充填", "エネルギーオーブは100シールドとオーバーシールドを与える。"),
    "BadMedicine" to Pair("悪い薬品", "ヘルスオーブは拾うと範囲10mの爆発を起こし、敵にダメージを与える。爆発に巻き込まれた敵一体につき30秒間50装甲値を得る。")
  )

  val calendarChallenges = mapOf(
    // フェアなゲーム (Even the Odds)
    "CalendarKillEnemiesEasy" to Pair("フェアなゲーム (簡単)", "250体の敵を倒す"),
    "CalendarKillEnemiesMedium" to Pair("フェアなゲーム (普通)", "500体の敵を倒す"),
    "CalendarKillEnemiesHard" to Pair("フェアなゲーム (難しい)", "1000体の敵を倒す"),

    // 刃にかけて (By the Blade)
    "CalendarKillEnemiesWithMeleeEasy" to Pair("刃にかけて (簡単)", "75体の敵を近接武器で倒す"),
    "CalendarKillEnemiesWithMeleeMedium" to Pair("刃にかけて (普通)", "150体の敵を近接武器で倒す"),
    "CalendarKillEnemiesWithMeleeHard" to Pair("刃にかけて (難しい)", "300体の敵を近接武器で倒す"),

    // 力の照明 (Demonstration of Power)
    "CalendarKillEnemiesWithAbilitiesEasy" to Pair("力の照明 (簡単)", "150体の敵をアビリティで倒す"),
    "CalendarKillEnemiesWithAbilitiesMedium" to Pair("力の照明 (普通)", "300体の敵をアビリティで倒す"),
    "CalendarKillEnemiesWithAbilitiesHard" to Pair("力の照明 (難しい)", "500体の敵をアビリティで倒す"),

    // エクスエクシマス (Ex-Eximus)
    "CalendarKillEximusEasy" to Pair("エクスエクシマス (簡単)", "10体のエクシマスを倒す"),
    "CalendarKillEximusMedium" to Pair("エクスエクシマス (普通)", "15体のエクシマスを倒す"),
    "CalendarKillEximusHard" to Pair("エクスエクシマス (難しい)", "20体のエクシマスを倒す"),

    // スカルドラ懲罰 (Punish Scaldra)
    "CalendarKillScaldraEnemiesEasy" to Pair("スカルドラ懲罰 (簡単)", "75体のスカルドラ部隊を倒す"),
    "CalendarKillScaldraEnemiesMedium" to Pair("スカルドラ懲罰 (普通)", "250体のスカルドラ部隊を倒す"),
    "CalendarKillScaldraEnemiesHard" to Pair("スカルドラ懲罰 (難しい)", "500体のスカルドラ部隊を倒す"),

    // 緊密に (Make It Personal)
    "CalendarKillScaldraEnemiesWithMeleeEasy" to Pair("緊密に (簡単)", "75体のスカルドラ部隊を近接武器で倒す"),
    "CalendarKillScaldraEnemiesWithMeleeMedium" to Pair("緊密に (普通)", "250体のスカルドラ部隊を近接武器で倒す"),
    "CalendarKillScaldraEnemiesWithMeleeHard" to Pair("緊密に (難しい)", "500体のスカルドラ部隊を近接武器で倒す"),

    // 衝撃と畏怖 (Shock and Awe)
    "CalendarKillScaldraEnemiesWithAbilitiesEasy" to Pair("衝撃と畏怖 (簡単)", "75体のスカルドラ部隊をアビリティで倒す"),
    "CalendarKillScaldraEnemiesWithAbilitiesMedium" to Pair("衝撃と畏怖 (普通)", "250体のスカルドラ部隊をアビリティで倒す"),
    "CalendarKillScaldraEnemiesWithAbilitiesHard" to Pair("衝撃と畏怖 (難しい)", "500体のスカルドラ部隊をアビリティで倒す"),

    // 感染体を一掃する (Purge the Infection)
    "CalendarKillTechrotEnemiesEasy" to Pair("感染体を一掃する (簡単)", "250体のテックロットを倒す"),
    "CalendarKillTechrotEnemiesMedium" to Pair("感染体を一掃する (普通)", "500体のテックロットを倒す"),
    "CalendarKillTechrotEnemiesHard" to Pair("感染体を一掃する (難しい)", "750体のテックロットを倒す"),

    // 電子廃棄物処理 (Electronic Waste Disposal)
    "CalendarKillTechrotEnemiesWithMeleeEasy" to Pair("電子廃棄物処理 (簡単)", "100体のテックロットを近接武器で倒す"),
    "CalendarKillTechrotEnemiesWithMeleeMedium" to Pair("電子廃棄物処理 (普通)", "200体のテックロットを近接武器で倒す"),
    "CalendarKillTechrotEnemiesWithMeleeHard" to Pair("電子廃棄物処理 (難しい)", "300体のテックロットを近接武器で倒す"),

    // 契約無効 (Null And Void)
    "CalendarKillTechrotEnemiesWithAbilitiesEasy" to Pair("契約無効 (簡単)", "100体のテックロットをアビリティで倒す"),
    "CalendarKillTechrotEnemiesWithAbilitiesMedium" to Pair("契約無効 (普通)", "200体のテックロットをアビリティで倒す"),
    "CalendarKillTechrotEnemiesWithAbilitiesHard" to Pair("契約無効 (難しい)", "300体のテックロットをアビリティで倒す"),

    // 戦車洗車 (Tankless Work)
    "CalendarDestroyTankEasy" to Pair("戦車洗車", "1体の戦車を破壊する"),
    "CalendarDestroyTankMedium" to Pair("戦車洗車", "1体の戦車を破壊する"),
    "CalendarDestroyTankHard" to Pair("戦車洗車", "1体の戦車を破壊する"),

    // 兵糧攻め (Starve the Beast)
    "CalendarDestroyPropsEasy" to Pair("兵糧攻め (簡単)", "75個のコンテナを破壊する"),
    "CalendarDestroyPropsMedium" to Pair("兵糧攻め (普通)", "150個のコンテナを破壊する"),
    "CalendarDestroyPropsHard" to Pair("兵糧攻め (難しい)", "300個のコンテナを破壊する")
  )

  val calendarRewards = mapOf(
    "CalendarKuvaBundleSmall" to "2,000 クバ",
    "CalendarMajorArtifactPack" to "アルケインパック",
    "CalendarVosforPack" to "ヴォスフォル",
    "CalendarRivenPack" to "Riven MOD",
    "CalendarArtifactPack" to "1999 アルケイン",
    "ResourceDropChance3DayStoreItem" to "リソースドロップブースター (3日間)",
    "ModDropChanceBooster3DayStoreItem" to "MODドロップブースター (3日間)",
    "AffinityBooster3DayStoreItem" to "アフィニティブースター (3日間)",
    "FormaBlueprint" to "フォーマの設計図",
    "FormaAuraBlueprint" to "オムニフォーマの設計図",
    "Forma" to "フォーマ",
    "FormaAura" to "オムニフォーマ",
    // "OmniFormaBlueprint" to "オムニフォーマの設計図",
    "WeaponUtilityUnlockerBlueprint" to "武器エクシラスアダプターの設計図",
    "WeaponUtilityUnlocker" to "武器エクシラスアダプター",
    "AdapterBlueprint" to "エクシラスアダプターの設計図",
    "Adapter" to "エクシラスアダプター",
    "PrimaryArcaneAdapter" to "プライマリ アルケインアダプター",
    "SecondaryArcaneAdapter" to "セカンダリ アルケインアダプター",
    "MeleeArcaneAdapter" to "近接アルケインアダプター",
    "OrokinCatalystBlueprint" to "オロキンカタリストの設計図",
    "OrokinReactorBlueprint" to "オロキンリアクターの設計図",
    
    // アルケイン
    "ArcaneBellicose" to "アルケイン ベリコーズ",
    "ArcaneTruculence" to "アルケイン トゥルキュレンス",
    "ArcaneImpetus" to "アルケイン インペタス",
    "ArcaneCrepuscular" to "アルケイン クレプスキュラー",
    "ArcaneCamisado" to "アルケイン カミサド",
    "PrimaryCrux" to "プライマリ クラックス",
    "SecondaryEnervate" to "セカンダリ エナベート",
    "MeleeDoughty" to "メレー ダウティ",
    
    // アルコンの欠片
    "ArchonShardRed" to "アルコンの欠片 (真紅)",
    "ArchonShardBlue" to "アルコンの欠片 (蒼天)",
    "ArchonShardAmber" to "アルコンの欠片 (琥珀)",
    "ArchonShardOrange" to "アルコンの欠片 (黄玉)",
    "ArchonShardPurple" to "アルコンの欠片 (紫紺)",
    "ArchonShardGreen" to "アルコンの欠片 (翠玉)",
    "ShardRedSimple" to "アルコンの欠片 (真紅)",
    "ShardBlueSimple" to "アルコンの欠片 (蒼天)",
    "ShardAmberSimple" to "アルコンの欠片 (琥珀)",
    "ShardOrangeSimple" to "アルコンの欠片 (黄玉)",
    "ShardPurpleSimple" to "アルコンの欠片 (紫紺)",
    "ShardGreenSimple" to "アルコンの欠片 (翠玉)",
    
    // 素材・通貨
    "Kuva" to "クバ",
    "Vosfor" to "ヴォスフォル",
    "Endo" to "Endo"
  )

  fun translateCalendarType(type: String): String = calendarEventTypes[type] ?: type
  fun translateCalendarSeason(season: String): String = calendarSeasons[season] ?: season

  fun translateCalendarContent(uniqueName: String): String? {
    val key = uniqueName.substringAfterLast("/")
    calendarUpgrades[key]?.first?.let { return it }
    calendarChallenges[key]?.first?.let { return it }
    calendarRewards[key]?.let { return it }
    return null
  }

  /**
   * 1999 Calendar 用のパス（uniqueName）から日本語のタイトルと説明のペアを返す。
   * 翻訳マップに存在しない場合は Pair(末尾のキー, "") を返す。
   */
  fun translateCalendarEvent(uniqueName: String): Pair<String, String> {
    val key = uniqueName.substringAfterLast("/")
    calendarChallenges[key]?.let { return it }
    calendarUpgrades[key]?.let { return it }
    calendarRewards[key]?.let { return Pair(it, "") }
    return Pair(key, "")
  }

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
  "Fomorian" to "フォーモリアン戦艦の脅威",
  "BalorFomorian" to "フォーモリアン戦艦の脅威"
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
    if (lower.contains("fomorian")) return "フォーモリアン戦艦の脅威"

    // リレー名としての翻訳を試みる
    val relayName = translateRelay(eventDescOrTag)
    if (relayName != eventDescOrTag) {
      return relayName
    }

    return eventDescOrTag
  }

  // リレーノードの日本語訳
  val relayNames = mapOf(
    "MercuryHUB" to "Larunda リレー (水星)",
    "VenusHUB" to "Vesper リレー (金星)",
    "EarthHUB" to "Strata リレー (地球)",
    "SaturnHUB" to "Kronia リレー (土星)",
    "EuropaHUB" to "Leonov リレー (ヨーロッパ)",
    "ErisHUB" to "Kuiper リレー (エリス)",
    "PlutoHUB" to "Orcus リレー (冥王星)",
    "TradeHUB1" to "Marooのバザー (地球)",
    "MarooBazaar" to "Marooのバザー (地球)",
    "TennoConHUB" to "Tennocon リレー",
    "TennoconHUB" to "Tennocon リレー",
    "/Lotus/Language/Locations/RelayStationTennoConB" to "TennoLive リレー",
    "/Lotus/Language/Locations/RelayStationTennoCon" to "Baro のTennoconリレー",
    "RelayStationTennoConB" to "TennoLive リレー",
    "RelayStationTennoCon" to "Baro のTennoconリレー"
  )

  /**
   * リレーノード名を日本語のリレー名に翻訳する
   */
  fun translateRelay(node: String): String {
    return relayNames[node] ?: relayNames.entries.find { (key, _) ->
      node.contains(key, ignoreCase = true)
    }?.value ?: translateNode(node)
  }

  /**
   * アイテムのTypeと日本語名からカテゴリ（「武器」「MOD」「Void レリック」「装飾品」「外装」「常設」）を判定する
   */
  fun getItemCategory(itemType: String, name: String): String {
    // 1. 常設
    if (itemType.contains("MummyQuest", ignoreCase = true) ||
        itemType.contains("BaroTreasureBox", ignoreCase = true) ||
        itemType.contains("FaePath", ignoreCase = true) ||
        name.contains("Fae Path", ignoreCase = true) ||
        name.contains("Inaros の砂", ignoreCase = true) ||
        name.contains("Void サープラス", ignoreCase = true) ||
        name.contains("砂嵐", ignoreCase = true)) {
      return "常設"
    }

    // 2. 外装
    if (itemType.contains("/Skins/", ignoreCase = true) ||
        itemType.contains("/AvatarImages/", ignoreCase = true) ||
        itemType.contains("/ArmorSets/", ignoreCase = true) ||
        itemType.contains("/Armor/", ignoreCase = true) ||
        itemType.contains("/Syandana/", ignoreCase = true) ||
        itemType.contains("/Scarves/", ignoreCase = true) ||
        itemType.contains("/Badges/", ignoreCase = true) ||
        itemType.contains("/Sigils/", ignoreCase = true) ||
        itemType.contains("/Emotes/", ignoreCase = true) ||
        name.contains("シャンダナ") || name.contains("スキン") ||
        name.contains("ヘルメット") || name.contains("エフェメラ") ||
        name.contains("アタッチメント") || name.contains("スーツ") ||
        name.contains("シジル") || name.contains("エモート") ||
        name.contains("アーマー") || name.contains("エンブレム") ||
        name.contains("アバター") || name.contains("グリフ")) {
      return "外装"
    }

    // 3. 武器
    if (itemType.contains("/Weapons/", ignoreCase = true) ||
        itemType.contains("/Recipes/Weapons/", ignoreCase = true) ||
        name.contains("Wraith") || name.contains("Vandal") ||
        name.contains("Prisma") || name.contains("Mara")) {
      return "武器"
    }

    // 4. MOD
    if (itemType.contains("/Mods/", ignoreCase = true) ||
        name.endsWith("Mod") || name.contains("Primed ")) {
      return "MOD"
    }

    // 5. Void レリック
    if (itemType.contains("/Projections/", ignoreCase = true) ||
        itemType.contains("/Relic", ignoreCase = true) ||
        name.contains("レリック")) {
      return "Void レリック"
    }

    // 6. 装飾品 (その他もここにフォールバック)
    return "装飾品"
  }

  // Modの装備対象（compatName）やタイプ（type）の日本語訳
  val modTargetTypes = mapOf(
    "WARFRAME" to "Warframe",
    "PRIMARY" to "プライマリ",
    "SECONDARY" to "セカンダリ",
    "MELEE" to "近接",
    "RIFLE" to "ライフル",
    "SHOTGUN" to "ショットガン",
    "PISTOL" to "ピストル",
    "ARCHWING" to "アークウイング",
    "ARCH-GUN" to "アークガン",
    "ARCH-MELEE" to "アーク近接",
    "SENTINEL" to "センチネル/ペット",
    "COMPANION" to "コンパニオン",
    "STANCE" to "スタンス",
    "PARAZON" to "パラゾン",
    "NECRAMECH" to "ネクロメカ",
    "AURA" to "オーラ",
    "EXILUS" to "エクシラス",
    "K-DRIVE" to "K-ドライブ",
    "AMP" to "アンプ",
    "ARCHGUN" to "アークガン",
    "ARCHMELEE" to "アーク近接"
  )

  /**
   * Modの装備対象を日本語に翻訳する
   */
  fun translateModTarget(target: String?): String? {
    if (target == null) return null
    val upper = target.uppercase().trim()
    modTargetTypes[upper]?.let { return it }
    if (upper.startsWith("/LOTUS/")) {
      val last = upper.substringAfterLast("/")
      return "${last.lowercase().replaceFirstChar { it.uppercase() }} 専用"
    }
    return target
  }

  /**
   * 数値を3桁カンマ区切りにする
   */
  fun formatComma(number: Long): String {
    val str = number.toString()
    val sb = StringBuilder()
    val len = str.length
    for (i in 0 until len) {
      if (i > 0 && (len - i) % 3 == 0) {
        sb.append(',')
      }
      sb.append(str[i])
    }
    return sb.toString()
  }

  fun formatComma(number: Int): String {
    return formatComma(number.toLong())
  }

  // --- レールジャック (CrewBattleNode) 専用のマッピングデータとヘルパー関数 ---
  data class CrewBattleNodeInfo(
    val englishName: String,
    val japaneseName: String,
    val missionType: String,
    val faction: String,
    val proxima: String
  )

  private val crewBattleNodes = mapOf(
    "CrewBattleNode502" to CrewBattleNodeInfo("Sover Strait", "ソバー海峡", "Skirmish", "Grineer", "Earth"),
    "CrewBattleNode509" to CrewBattleNodeInfo("Iota Temple", "イオタ神殿", "Skirmish", "Grineer", "Earth"),
    "CrewBattleNode518" to CrewBattleNodeInfo("Ogal Cluster", "オーガル・クラスター", "Skirmish", "Grineer", "Earth"),
    "CrewBattleNode519" to CrewBattleNodeInfo("Korm's Belt", "コームの帯", "Skirmish", "Grineer", "Earth"),
    "CrewBattleNode522" to CrewBattleNodeInfo("Bendar Cluster", "ベンダー・クラスター", "Skirmish", "Grineer", "Earth"),

    "CrewBattleNode503" to CrewBattleNodeInfo("Bifrost Echo", "ビフレストのエコー", "Extermination", "Corpus", "Venus"),
    "CrewBattleNode511" to CrewBattleNodeInfo("Beacon Shield Ring", "ビーコン・シールドリング", "Volatile", "Corpus", "Venus"),
    "CrewBattleNode512" to CrewBattleNodeInfo("Orvin-Haarc", "オービンハーク", "Spy", "Corpus", "Venus"),
    "CrewBattleNode513" to CrewBattleNodeInfo("Vesper Strait", "ヴェスパー海峡", "Orphix", "Corpus", "Venus"),
    "CrewBattleNode514" to CrewBattleNodeInfo("Falling Glory", "フォーリング・グローリー", "Defense", "Corpus", "Venus"),
    "CrewBattleNode515" to CrewBattleNodeInfo("Luckless Expanse", "ラックレス大空間", "Survival", "Corpus", "Venus"),

    "CrewBattleNode501" to CrewBattleNodeInfo("Mordo Cluster", "モルド・クラスター", "Skirmish", "Grineer", "Saturn"),
    "CrewBattleNode530" to CrewBattleNodeInfo("Kasio's Rest", "カシオの安息地", "Skirmish", "Grineer", "Saturn"),
    "CrewBattleNode533" to CrewBattleNodeInfo("Nodo Gap", "ノード間隙", "Skirmish", "Grineer", "Saturn"),
    "CrewBattleNode534" to CrewBattleNodeInfo("Lupal Pass", "ルパル峠", "Skirmish", "Grineer", "Saturn"),
    "CrewBattleNode535" to CrewBattleNodeInfo("Vand Cluster", "ヴァンド・クラスター", "Skirmish", "Grineer", "Saturn"),

    "CrewBattleNode504" to CrewBattleNodeInfo("Arva Vector", "アルバ・ベクトル", "Defense", "Corpus", "Neptune"),
    "CrewBattleNode516" to CrewBattleNodeInfo("Nu-gua Mines", "ヌグア採掘場", "Extermination", "Corpus", "Neptune"),
    "CrewBattleNode521" to CrewBattleNodeInfo("Enkidu Ice Drifts", "エンキドゥ氷流", "Survival", "Corpus", "Neptune"),
    "CrewBattleNode523" to CrewBattleNodeInfo("Mammon's Prospect", "マモンの展望地", "Orphix", "Corpus", "Neptune"),
    "CrewBattleNode524" to CrewBattleNodeInfo("Sovereign Grasp", "サヴレン・グラスプ", "Volatile", "Corpus", "Neptune"),
    "CrewBattleNode525" to CrewBattleNodeInfo("Brom Cluster", "ブロム・クラスター", "Spy", "Corpus", "Neptune"),

    "CrewBattleNode526" to CrewBattleNodeInfo("Khufu Envoy", "クフ使節", "Orphix", "Corpus", "Pluto"),
    "CrewBattleNode527" to CrewBattleNodeInfo("Seven Sirens", "セブン・サイレンス", "Extermination", "Corpus", "Pluto"),
    "CrewBattleNode528" to CrewBattleNodeInfo("Obol Crossing", "オボル交差路", "Defense", "Corpus", "Pluto"),
    "CrewBattleNode529" to CrewBattleNodeInfo("Profit Margin", "プロフィット・マージン", "Volatile", "Corpus", "Pluto"),
    "CrewBattleNode531" to CrewBattleNodeInfo("Fenton's Field", "フェントンの野", "Survival", "Corpus", "Pluto"),
    "CrewBattleNode536" to CrewBattleNodeInfo("Peregrine Axis", "ペレグリン軸", "Spy", "Corpus", "Pluto"),

    "CrewBattleNode505" to CrewBattleNodeInfo("Ruse War Field", "ルース戦域", "Skirmish", "Grineer", "Veil"),
    "CrewBattleNode510" to CrewBattleNodeInfo("Gian Point", "ジャイアン・ポイント", "Skirmish", "Grineer", "Veil"),
    "CrewBattleNode538" to CrewBattleNodeInfo("Calabash", "カラバッシュ", "Extermination", "Corpus", "Veil"),
    "CrewBattleNode539" to CrewBattleNodeInfo("Numina", "ヌミナ", "Volatile", "Corpus", "Veil"),
    "CrewBattleNode540" to CrewBattleNodeInfo("Arc Silver", "アーク・シルバー", "Defense", "Corpus", "Veil"),
    "CrewBattleNode541" to CrewBattleNodeInfo("Erato", "エラト", "Orphix", "Corpus", "Veil"),
    "CrewBattleNode542" to CrewBattleNodeInfo("Lu-yan", "ルーヤン", "Survival", "Corpus", "Veil"),
    "CrewBattleNode543" to CrewBattleNodeInfo("Sambir Cloud", "サンビル星雲", "Spy", "Corpus", "Veil"),
    "CrewBattleNode550" to CrewBattleNodeInfo("Nsu Grid", "ヌスー・グリッド", "Skirmish", "Grineer", "Veil"),
    "CrewBattleNode551" to CrewBattleNodeInfo("Ganalen's Grave", "ガナレンの墓", "Skirmish", "Grineer", "Veil"),
    "CrewBattleNode552" to CrewBattleNodeInfo("Rya", "リア", "Skirmish", "Grineer", "Veil"),
    "CrewBattleNode553" to CrewBattleNodeInfo("Flexa", "フレクサ", "Skirmish", "Grineer", "Veil"),
    "CrewBattleNode554" to CrewBattleNodeInfo("H-2 Cloud", "H-2 星雲", "Skirmish", "Grineer", "Veil"),
    "CrewBattleNode555" to CrewBattleNodeInfo("R-9 Cloud", "R-9 星雲", "Skirmish", "Grineer", "Veil")
  )

  fun getCrewBattleNodeInfo(nodeKey: String): CrewBattleNodeInfo? {
    return crewBattleNodes[nodeKey]
  }

  fun getCrewBattleLevelRange(proxima: String): Pair<Int, Int> {
    return when (proxima.lowercase()) {
      "earth" -> Pair(40, 50)
      "venus" -> Pair(45, 55)
      "saturn" -> Pair(60, 70)
      "neptune" -> Pair(65, 75)
      "pluto" -> Pair(80, 90)
      "veil" -> Pair(90, 100)
      else -> Pair(0, 0)
    }
  }
}


