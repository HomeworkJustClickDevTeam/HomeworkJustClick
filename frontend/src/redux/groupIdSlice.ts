import {createSlice, PayloadAction} from "@reduxjs/toolkit";
import {RootState} from "./store";

type GroupIdSliceType = number | null
const initialState = null as GroupIdSliceType

export const groupIdSlice = createSlice({
    name: 'groupId',
    initialState,
    reducers: {
        setGroupId: (state, action: PayloadAction<GroupIdSliceType>) => action.payload
    }
})

export const {setGroupId} = groupIdSlice.actions
export const selectGroupId = (state: RootState) => state.groupId
export default groupIdSlice.reducer