# Primordialis Testudo Clan

The **Primordialis Testudo Clan** is the first test cultivation clan.

It is intended to be integrated into vanilla villages before custom cities/capitals are implemented.

## Legend

The clan descends from a cultivator who once saved a tortoise spirit beast.

In return, the beast passed down a complete Earth-aligned inheritance.

The resulting method is intentionally slow, stable, and quality-focused.

## Core Inheritance

The clan's core inheritance is:

**Primordialis Testudo Longevity Art**

### Identity

- Element: Earth
- Path trait: Neutral
- Supported progression: Mortal/Tempered Body through **Transcendent**
- Cultivation speed: slow
- Foundation quality: high
- Lifespan: unusually high
- Defensive/body-tempering emphasis
- Tortoise bloodlines accelerate compatibility/progression

The inheritance can therefore carry a player from their first entry into cultivation through Transcendent without requiring a method switch.

## Design Purpose

This clan is ideal as the first test faction because it stresses systems other than raw speed:

- manuals/inheritances,
- Earth affinity,
- body tempering,
- lifespan,
- foundation quality,
- bloodline synergy,
- Masters and disciples,
- village integration,
- full-method realm ceilings.

## Clan Hierarchy

The Primordialis Testudo Clan has a defined institutional hierarchy:

1. **Clan Leader** — exactly 1
2. **Elders** — exactly 6
3. **Custodian** — exactly 1 senior office
4. **Masters**
5. **Senior Disciples**
6. **Junior Disciples**
7. **Servants** — Mortals

The Clan Leader is the sole head of the clan.

The six Elders form the senior advisory/executive layer. In the Clan Leader Hall they receive commands from the Clan Leader, advise the leader, and assist with clan administration.

The Custodian is a senior office below the Elders in broad authority and is the primary overseer of the clan library.

Masters teach and supervise disciples.

A sufficiently high-ranking disciple can become eligible for direct tutelage under the Clan Leader. The current implementation uses **Senior Disciple** as the provisional minimum rank for being considered; additional merit, talent, service, relationship, or quest requirements can be added later.

Servants are mortal clan workers rather than cultivator disciples.

## Village Integration

Initial implementation should use vanilla villages.

A qualifying village can receive a small Primordialis Testudo compound containing some combination of:

- training courtyard,
- clan hall,
- manual/inheritance room,
- meditation chamber,
- Earth-aligned training blocks,
- elder/master NPC,
- disciple NPCs.

The clan should make a village safer than wilderness without making it completely immune to hostile cultivators or beasts.


## Admission by Service

The clan does not hand its inheritance to a random Mortal.

A prospective disciple proves reliability by completing practical service quests for the clan and village.

Initial admission quest pool:

- repair damaged parts of the clan compound,
- act as an errand runner and collect clan weapons from the Blacksmith,
- collect pills and medicine from the Alchemist,
- collect baked goods from the Bakery,
- collect Noodle Soup from the Inn.

These jobs deliberately connect the clan to the settlement economy. They also give a Mortal useful tasks that do not require surviving high-level wilderness combat.

### Service Merit

The implementation uses **Service Merit** as the technical admission progress value.

Current prototype weights:

- repairing the clan compound: 2 merit,
- ordinary delivery/errand: 1 merit.

Current prototype admission threshold: 3 merit.

These numbers are tuning values and may be changed without altering the lore.

Once accepted, the player enters as a **Junior Disciple** and receives access to the Primordialis Testudo Longevity Art.

The final NPC interaction should still involve an Elder/Master formally accepting the player rather than silently changing membership in the background.


## Tortoise Bloodline

The clan's cultivation art has strong synergy with Tortoise-family bloodlines, but the clan does not provide one to every disciple.

Player bloodlines must come from rare/specific progression sources such as quests, ancient cultivator tombs, monuments, auctions, spirit pacts, or compatible spirit-beast refinement.

The early defined grade is **Earth Rank**, whose effective power remains competitive through Saint.

A stronger compatible Tortoise lineage may later overwrite it. Unrelated lineages such as Tiger bloodlines cannot simply replace it and instead cause bloodline conflict.

See `docs/design/bloodlines.md`.


## Clan Facilities

The first clan compound is designed around these institutional spaces:

- **Clan Leader Hall** — command, Elder meetings, governance, formal audiences.
- **Hall of Punishment** — discipline, sanctions, punishment proceedings.
- **Clan Library** — history, factual records, fundamentals, and sparse technique manuals.
- **Training Court** — disciple practice, sparring, physical training, technique training.
- **Meditation Chamber** — calm cultivation.
- **Inheritance Room** — protected inheritance/manual access.
- **Disciple Quarters**
- **Servant Quarters**

The implementation may combine some rooms in the first small village structure for practical world-generation size, but the logical facilities remain separate systems.


## Clan Library

The library contains several kinds of books:

- clan and regional history,
- factual/reference works,
- cultivation fundamentals,
- technique manuals.

Technique manuals are deliberately **sparse**. The library must not function as a free technique-selection menu.

### Possession is not permission

A player may physically carry multiple books or even steal books from the library, but possession does not make them usable.

A carried library book requires authorization from the library overseer before its contents can be used outside the permitted library context.

The Custodian is the normal library overseer. Elders and the Clan Leader retain senior authority to approve access when appropriate.

### One verified take-out book

Each clan member may have only **one currently verified take-out book** from this library.

When the overseer verifies a different book:

- the previous authorization is replaced,
- the newly verified book becomes the only usable take-out book,
- every other carried clan-library book remains inaccessible/unusable.

Therefore stealing or carrying many manuals gives no technique advantage by itself.

### Rank and technique restrictions

Verification does not bypass clan rank restrictions.

A Junior Disciple cannot use a book reserved for Senior Disciples or Masters merely because they possess the item.

Technique manuals can also require additional eligibility such as:

- cultivation realm,
- affinity,
- technique prerequisites,
- Master approval,
- clan merit,
- quest completion,
- compatibility with the disciple's current cultivation.

This prevents players from simply choosing whichever rare technique they want from a shelf.

### Library authorization persistence

Take-out authorization persists with the character and is server-authoritative.

The player save stores the authorized book separately from physical inventory items so copying, stealing, dropping, or moving an item does not manufacture permission.
