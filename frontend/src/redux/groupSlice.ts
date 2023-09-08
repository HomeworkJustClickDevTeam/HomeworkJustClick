import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { GroupInterface } from "../types/GroupInterface"
import { RootState } from "./store"

type GroupSliceType = GroupInterface | null
const initialState = null as GroupSliceType
export const groupSlice = createSlice({
  name:'group',
  initialState,
  reducers:{
    setGroup:(state, action:PayloadAction<GroupSliceType>) => action.payload
  }
})

export const {setGroup} = groupSlice.actions
export const selectGroup = (state:RootState) => state.group
export default groupSlice.reducer