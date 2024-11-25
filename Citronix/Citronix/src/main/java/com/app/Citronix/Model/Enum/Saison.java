package com.app.Citronix.Model.Enum;

public enum Saison {
    PRINTEMPS,
    ETE,
    AUTOMNE,
    HIVER;

        @Override
        public String toString() {
            return name().toLowerCase();
        }
}
