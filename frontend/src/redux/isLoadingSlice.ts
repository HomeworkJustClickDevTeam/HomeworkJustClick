import { createSlice } from "@reduxjs/toolkit"
import { RootState } from "./store"

const initialState: boolean = false

export const isLoadingSlice = createSlice({
  name: 'isLoading',
  initialState,
  reducers:{
    setIsLoading: (state, action) => state = action.payload
  }
})

export const {setIsLoading} = isLoadingSlice.actions
export const selectIsLoading = (state:RootState) => state.isLoading
export default isLoadingSlice.reducer