import { Link, useLocation } from "react-router-dom"
import { useContext } from "react"
import GroupContext from "../../GroupRoleContext"
import {PropsForGroupItem} from "../../types/types";



function GroupHeader({ group }: PropsForGroupItem) {
  const { role } = useContext(GroupContext)
  const location = useLocation();
  const locationSplit = location.pathname.split("/");
  return (
    <div>
        <p>{group.name}</p>
        <p>{group.description}</p>
      <header>
        
      <div className="text-[22px] font-lato font-medium text-center text-font_gray ">
          <ul className="flex flex-wrap -mb-px">
              <li className={ locationSplit[3] === "assignments" || locationSplit[3] === "solutions" ?  "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                  <Link to="assignments" className="inline-block w-[170px] p-4  rounded-t-lg "> Zadania</Link>
              </li>
              <li className={ locationSplit[3] === "users" ?  "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                <Link to="users" className="inline-block w-[170px] p-4  rounded-t-lg " > Osoby </Link>
              </li>
              <li className={ locationSplit[3] === "#" ?  "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                  <Link to="#" className="inline-block w-[170px] p-4  rounded-t-lg "> Oceny</Link>
              </li>
          </ul>
      </div>



        {role === "Student" ? (
          <Link to="assignments/todo">Do wykonania</Link>
        ) : (
          <Link to="solutions/uncheck">Do sprawdzenia</Link>
        )}
        {role === "Student" ? (
          <Link to="assignments/done">Zrobione</Link>
        ) : (
          <Link to="solutions/check">Sprawdzone</Link>
        )}
        {role === "Student" ? (
          <Link to="assignments/expired">Zalagle</Link>
        ) : (
          <Link to="solutions/late">Spóznione</Link>
        )}
        {role === "Teacher" && <Link to="settings">Ustawienia</Link>}
      </header>
    </div>
  )
}
export default GroupHeader
