import {Outlet, useNavigate, useParams} from "react-router-dom"
import React from "react"
import {addStudentToGroupPostgresService} from "../../services/postgresDatabaseServices"
import GroupHeader from "./GroupHeader"
import Loading from "../animations/Loading"
import {selectUserState} from "../../redux/userStateSlice"
import {selectGroup} from "../../redux/groupSlice"
import {selectRole} from "../../redux/roleSlice"
import {useAppSelector} from "../../types/HooksRedux"
import {useGetGroupAndRole} from "../customHooks/useGetGroupAndRole"

function GroupPage() {
    const {idGroup} = useParams()
    const userState = useAppSelector(selectUserState)
    const group = useAppSelector(selectGroup)
    const role = useAppSelector(selectRole)
    const navigate = useNavigate()
    useGetGroupAndRole(idGroup as unknown as number, userState?.id)

    if (group === undefined || group === null) {
        return <Loading/>
    }

    async function addToGroup() {
        await addStudentToGroupPostgresService(userState?.id as unknown as string, group?.id as unknown as string)
            .then(() => navigate("/"))
    }

    const goToLoginPage = () => {
        navigate("/login", {state: {groupId: idGroup}})
    }

    if (!userState?.id) {
        return (
            <div className='flex flex-col items-center mt-16'>
                <p className='flex  font-semibold underline underline-offset-2 decoration-main_blue text-3xl'>Nie jesteś
                    zalogowany zaloguj się!</p>
                <button onClick={() => goToLoginPage()}
                        className=" mt-16 w-52 mr-2 bg-main_blue text-white px-4 py-2 rounded-md hover:text-hover_blue hover:bg-white hover:border hover:border-solid hover:border-main_blue text-xl">Zaloguj
                </button>
            </div>
        )
    }

    if (role === "User not in group") {
        return (
            <div className='flex flex-col items-center mt-16'>
              <p className='flex  font-semibold underline underline-offset-2 decoration-main_blue text-3xl mb-12'>Nie ma Cię jeszcze w tej grupie</p>
              <div className='flex flex-col w-[500px] border-4 border-main_blue rounded-md px-6 pt-12 pb-4 items-center text-xl'>
                <p className='text-xl mb-4 '><span className='font-semibold'>Nazwa grupy: </span>  {group.name}</p>
                <p className='font-semibold mb-2'>Opis grupy: </p>
                <p className='text-center '>  {group.description}</p>
                <button onClick={() => addToGroup()} className=" mt-14 w-52 mr-2 bg-main_blue border border-main_blue text-white px-4 py-2 rounded-md hover:text-hover_blue hover:bg-white hover:border hover:border-solid hover:border-main_blue text-xl"> Dołącz do grupy</button>
              </div>
            </div>
        )
    }


    return (
        <div>
            <GroupHeader key={group.id}/>
            <Outlet/>
        </div>
    )
}

export default GroupPage
