import { createSlice, PayloadAction } from "@reduxjs/toolkit"
import { UserInterface } from "../types/UserInterface"
import { AppDispatch, RootState } from "./store"
import { getUser, loginUser} from "../services/otherServices"
import { setIsLoadingInReducer } from "./isLoadingSlice"

type UserStateSliceType = UserInterface | null
export const userStateSlice = createSlice({
  name: 'userState',
  initialState: getUser() as UserStateSliceType,
  reducers: {
    logOut: (state) => {
      localStorage.removeItem("user")
      return null
    },
    setUser: (_, action: PayloadAction<UserStateSliceType>) => action.payload
  },
  extraReducers:(builder) =>{
    builder.addCase(loginUser.pending, ()=> {
      setIsLoadingInReducer(true)
      return null
    })
    builder.addCase(loginUser.rejected, () => null)
    builder.addCase(loginUser.fulfilled,(_, action) => action.payload)
  }
})

export const {logOut, setUser} = userStateSlice.actions
export const selectUserState = (state: RootState) => state.userState
export default userStateSlice.reducer