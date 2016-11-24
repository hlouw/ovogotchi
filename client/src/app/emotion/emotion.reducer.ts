import { ActionReducer, Action } from '@ngrx/store';

export interface OverallState {
    state: EmotionalState;
    wellbeing: number;
}

export enum EmotionalState {
    HAPPY,
    NEUTRAL,
    SAD,
    SICK,
    HUNGRY,
    ANGRY,
    LONELY,
    STRESSED
}

export const STATE_UPDATE = 'STATE_UPDATE';

const INITIAL_STATE: OverallState = {
    state: EmotionalState.NEUTRAL,
    wellbeing: 70
};

const stateMap = {
  'happy': EmotionalState.HAPPY,
  'neutral': EmotionalState.NEUTRAL,
  'sad': EmotionalState.SAD,
  'angry': EmotionalState.ANGRY,
  'ill': EmotionalState.ILL
};

export const emotionReducer: ActionReducer<OverallState> = (state: OverallState = INITIAL_STATE, action: Action) => {
    switch (action.type) {
        case STATE_UPDATE:
          return Object.assign({}, state, {
            state: stateMap[action.payload.state.toLowerCase()],
            wellbeing: action.payload.wellbeing
          });

        default:
          return state;
    }
};
