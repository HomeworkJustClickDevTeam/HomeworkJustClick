import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { RootState } from "./store"


type RoleSliceType = string|null
const initialState = null as RoleSliceType

export const roleSlice = createSlice({
  name: 'role',
  initialState,
  reducers:{
    setRole:(state, action:PayloadAction<RoleSliceType>) => action.payload
  }
})

export const {setRole} = roleSlice.actions
export const selectRole = (state:RootState) => state.role
export default roleSlice.reducer