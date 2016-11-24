import { ActionReducer, Action } from '@ngrx/store';

export interface OverallState {
    state: EmotionalState;
    wellbeing: number;
}

export enum EmotionalState {
    HAPPY,
    NEUTRAL,
    SAD,
    ANGRY,
    ILL
}

export const WELLBEING = 'WELLBEING';
export const STATE = 'STATE';

const INITIAL_STATE: OverallState = {
    state: EmotionalState.NEUTRAL,
    wellbeing: 70
};

export const emotionReducer: ActionReducer<OverallState> = (state: OverallState = INITIAL_STATE, action: Action) => {
    switch (action.type) {
        case WELLBEING: return INITIAL_STATE;
    }
};
