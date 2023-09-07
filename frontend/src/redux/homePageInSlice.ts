import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { RootState } from "./store"

const initialState: boolean = true
export const homePageInSlice = createSlice({
  name: 'homePageIn',
  initialState,
  reducers:{
    setHomePageIn:(state, action:PayloadAction<boolean>) => action.payload
  }
})

export const {setHomePageIn} = homePageInSlice.actions
export const selectHomePageIn = (state:RootState) => state.homePageIn
export default homePageInSlice.reducer