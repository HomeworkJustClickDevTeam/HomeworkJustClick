import GroupUsersDisplayer from "./GroupUsersDisplayer"
import {useParams} from "react-router-dom"
import {useContext} from "react";
import ApplicationStateContext from "../../contexts/ApplicationStateContext";

function GroupUsersPage() {
  const {applicationState} = useContext(ApplicationStateContext)
  return (
    <>
      <GroupUsersDisplayer groupId={applicationState?.group?.id as unknown as string}/>
    </>
  )
}

export default GroupUsersPage
