import {useParams} from "react-router-dom";
import GroupAppearanceSettings from "./GroupAppearanceSettings";
import GroupUsersSettings from "./GroupUsersSettings";
import GroupGeneralSettings from "./GroupGeneralSettings";
import React, {useContext, useState} from "react";
import userContext from "../../../UserContext";
import Loading from "../../animations/Loading";

export default function GroupSettings(){
  const {loggedIn, userState} = useContext(userContext);
  const [loading, setLoading] = useState<boolean>(false)

  if(loading){
    return <Loading></Loading>
  }
  if (!loggedIn) {
    return <>Not log in</>
  }
  return(
    <div>
      <p className=''>Ustawienia</p>
      <dl>
        <dt>Ogólne</dt>
          <dd><GroupGeneralSettings/></dd>
        <dt>Użytkownicy</dt>
          <dd><GroupUsersSettings/></dd>
        <dt>Kolor</dt>
          <dd><GroupAppearanceSettings/></dd>
      </dl>
    </div>
  )
}