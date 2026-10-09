package io.github.artificialturtill.foundation.content;

/** A class of one power type (DBC: Martial Artist, Spiritualist, Warrior). Bonuses are percent. */
public record CharacterClass(String id, String powerType, int[] statBonus, int[] recoveryBonus, int order) {
    public int statBonus(int stat) { return stat >= 0 && stat < statBonus.length ? statBonus[stat] : 0; }
    /** resource: 0 body, 1 energy, 2 stamina. */
    public int recoveryBonus(int resource) { return resource < recoveryBonus.length ? recoveryBonus[resource] : 0; }
}
