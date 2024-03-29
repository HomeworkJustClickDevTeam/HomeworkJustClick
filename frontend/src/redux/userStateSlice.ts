import {createSlice, PayloadAction} from "@reduxjs/toolkit"
import {UserInterface} from "../types/UserInterface"
import {RootState} from "./store"
import {loginUser} from "../services/otherServices"
import {setIsLoadingInReducer} from "./isLoadingSlice"

type UserStateSliceType = UserInterface | null
export const userStateSlice = createSlice({
    name: 'userState',
    initialState: null as UserStateSliceType,
    reducers: {
        logOut: (_) => {
            localStorage.removeItem("user")
            return null
        },
        setUser: (_, action: PayloadAction<UserStateSliceType>) => action.payload
    },
    extraReducers: (builder) => {
        builder.addCase(loginUser.pending, () => {
            setIsLoadingInReducer(true)
            return null
        })
        builder.addCase(loginUser.rejected, () => {
            setIsLoadingInReducer(false)
            localStorage.removeItem("user")
            return null
        })
        builder.addCase(loginUser.fulfilled, (_, action) => {
            setIsLoadingInReducer(false)
            if (action.payload.response !== undefined &&
                action.payload.response.status in [...Array.from({length: 299 - 200 + 1}, (_, index) => index + 200)] === false) {
                return null
            } else {
                return action.payload
            }
        })
    }
})

export const {logOut, setUser} = userStateSlice.actions
export const selectUserState = (state: RootState) => state.userState
export default userStateSlice.reducer