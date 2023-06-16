import {useParams} from "react-router-dom";
import {useContext, useEffect, useState} from "react";
import {UserToShow} from "../../../types/types";
import postgresqlDatabase from "../../../services/postgresDatabase";
import {AxiosError} from "axios";
import Loading from "../../animations/Loading";
import userContext from "../../../UserContext";

export default function UserProfileInGroup(){
  const {userProfileId,id} = useParams()
  const [userProfile, setUserProfile] = useState<UserToShow | undefined>(undefined)
  const [isTeacher, setIsTeacher] = useState<boolean | undefined>(undefined)
  const {userState} = useContext(userContext)

  useEffect(()=>{
    postgresqlDatabase
      .get(`/user/${userProfileId}`)
      .then((response) => setUserProfile(response.data))
      .catch((error:AxiosError) => console.log(error))
  }, [])

  if(!userProfile || typeof isTeacher === undefined){
    return <Loading/>
  }
  return(
    <ul>
      <li>{userProfile.firstname} {userProfile.lastname}</li>
      <li>Indeks: {userProfile.index}</li>
      <li></li>
    </ul>
  )
}