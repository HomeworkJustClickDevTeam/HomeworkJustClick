import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { UserInterface } from "../types/UserInterface"
import { RootState } from "./store"

type UserStateSliceType = UserInterface | null
export const userStateSlice = createSlice({
  name: 'userState',
  initialState: null as UserStateSliceType,
  reducers: {
    logOut: () => null,
    setUser: (state, action: PayloadAction<UserStateSliceType>) => action.payload
  }
})

export const {logOut, setUser} = userStateSlice.actions
export const selectUserState = (state: RootState) => state.userState
export default userStateSlice.reducer