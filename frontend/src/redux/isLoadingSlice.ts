import { createDraftSafeSelector, createSlice } from "@reduxjs/toolkit"
import { AppDispatch, RootState } from "./store"

const initialState: boolean = false

export const isLoadingSlice = createSlice({
  name: 'isLoading',
  initialState,
  reducers:{
    setIsLoading: (_, action) => action.payload
  }
})

export const {setIsLoading} = isLoadingSlice.actions

export const setIsLoadingInReducer = (newState:boolean) => {
  return (dispatch:AppDispatch) => {
    dispatch(setIsLoading(newState))
  }
}
export const selectIsLoading = (state:RootState):boolean => state.isLoading //Use when you want to obtain the state of isLoading
export default isLoadingSlice.reducer