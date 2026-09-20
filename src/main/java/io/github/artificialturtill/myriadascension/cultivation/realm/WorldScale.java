package io.github.artificialturtill.myriadascension.cultivation.realm;

public enum WorldScale {
    STARTING_WORLD(WorldTransitionType.NONE),
    PLANET(WorldTransitionType.ASCENSION),
    STARFIELD(WorldTransitionType.TRAVEL),
    STAR_BOUNDARY(WorldTransitionType.ASCENSION),
    OUTER_UNIVERSE(WorldTransitionType.ASCENSION),
    MULTIVERSE(WorldTransitionType.ASCENSION);

    private final WorldTransitionType transitionFromPrevious;

    WorldScale(WorldTransitionType transitionFromPrevious) {
        this.transitionFromPrevious = transitionFromPrevious;
    }

    public WorldTransitionType transitionFromPrevious() {
        return transitionFromPrevious;
    }
}
