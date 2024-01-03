import {useAppDispatch} from "../../types/HooksRedux";
import {useNavigate} from "react-router-dom";
import {setGroupId} from "../../redux/groupIdSlice";


function GroupLogIn(props: {
    groupId: number
}) {
    const dispatch = useAppDispatch()
    const navigate = useNavigate()
    dispatch(setGroupId(props.groupId))

    const goToLoginPage = () => {
        navigate("/login")
    }

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

export default GroupLogIn