import { ActionReducer, Action } from '@ngrx/store';

export interface OverallState {
    state: EmotionalState;
    wellbeing: number;
    thought?: string;
}

export enum EmotionalState {
    HAPPY,
    NEUTRAL,
    SAD,
    ILL,
    ANGRY,
    LONELY
}

export const STATE_UPDATE = 'STATE_UPDATE';

const INITIAL_STATE: OverallState = {
    state: EmotionalState.ANGRY,
    wellbeing: 70
};

const stateMap = {
  'happy': EmotionalState.HAPPY,
  'neutral': EmotionalState.NEUTRAL,
  'sad': EmotionalState.SAD,
  'ill': EmotionalState.ILL,
  'angry': EmotionalState.ANGRY,
  'lonely': EmotionalState.LONELY
};

export const emotionReducer: ActionReducer<OverallState> = (state: OverallState = INITIAL_STATE, action: Action) => {
    switch (action.type) {
        case STATE_UPDATE:
          let newState = Object.assign({}, state, {
            state: stateMap[action.payload.state.toLowerCase()],
            wellbeing: action.payload.wellbeing,
            thought: action.payload.thought
          });
          console.log('NEW STATE: ' + JSON.stringify(newState));
          return newState;

        default:
          return state;
    }
};
