import { Link, useLocation } from "react-router-dom"
import React, {useEffect, useState} from "react"
import { colorsArray } from "../../assets/colors"
import Loading from "../animations/Loading"
import { selectGroup } from "../../redux/groupSlice"
import { selectRole } from "../../redux/roleSlice"
import { useAppSelector } from "../../types/HooksRedux"
import {FaCaretDown} from "react-icons/fa";
import {GroupHomePageDisplayer} from "./GroupHomePageDisplayer";

//import {theme.colors.colorsArray: string[]} = require("../../../tailwind.config")

function GroupHeader({}:{key:number}) {
  const location = useLocation();
  const locationSplit = location.pathname.split("/");
  const group= useAppSelector(selectGroup)
  const role = useAppSelector(selectRole)
  const [btnStudentName, setBtnStudentName] = useState('Do wykonania')
  const [btnTeacherName, setBtnTeacherName] = useState('Do sprawdzenia')
  const [isOpen, setIsOpen] = useState(false)

  useEffect(() => {
  }, [group]);

  if(group !== null) {
    return (
      <div>
        <div className='relative flex text-white justify-center select-none'>
          <div className={`select-none w-[85%] mt-8 h-36 rounded-xl ${colorsArray[group?.color]}`}>
            {/* <div className='w-[85%] mt-8 h-36 rounded-xl bg-[#59007f] bg-[#006400] bg-[#800000] bg-[#9A6324] bg-[#469990] bg-[#000075] bg-[#f58231] bg-[#546A7B] bg-[#000000] bg-[#BB9F06] bg-[#3F2A2B] bg-[#565656] bg-[#99621E] bg-[#739E82] bg-[#550C18] bg-[#136F63] bg-[#EF7A85] bg-[#D664BE] bg-[#642CA9] bg-[#03312E]'> */}
            <article className='absolute bottom-0 mb-6 ml-4'>
              <p className='text-3xl'>{group.name}</p>
              <p className='text-xl mt-1'>{group.description}</p>
            </article>
          </div>
        </div>
        <header className='flex inline-block mt-4 mx-[7.5%] justify-between'>
          <div className="text-[22px] font-lato font-medium text-center text-font_gray">
            <ul className="flex flex-wrap -mb-px">
              <li
                className={locationSplit[3] === "assignments" || locationSplit[3] === "solutions" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                <Link to="assignments" className="inline-block w-[170px] p-4  rounded-t-lg "> Zadania</Link>
              </li>
              <li
                className={locationSplit[3] === "users" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                <Link to="users" className="inline-block w-[170px] p-4  rounded-t-lg "> Osoby </Link>
              </li>
              <li
                className={locationSplit[3] === "#" ? "border-main_blue border-b-[3px] rounded bg-main_blue bg-opacity-5 text-black" : "hover:bg-hover_gray"}>
                <Link to="#" className="inline-block w-[170px] p-4  rounded-t-lg "> Oceny</Link>
              </li>
            </ul>
          </div>
          <div className='relative float-right flex h-16 items-center gap-5 '>
            {role === "Student"  ? (<button className='flex inline underline' onClick={() => setIsOpen(el => !el)}>{btnStudentName}<FaCaretDown
                className=' ml-1 h-full w-[9px] items-center mt-2'/>
            </button>) : (<button className='flex inline underline' onClick={() => setIsOpen(el => !el)}>{btnTeacherName}<FaCaretDown
                className=' ml-1 h-full w-[9px] items-center mt-2'/>
            </button>)}

            {isOpen && ( role === "Student" ? (
                <div
                    className=' absolute left-4 w-40 z-10  mt-36 origin-top-right bg-white border border-hover_gray rounded-md shadow-lg '>

                  <Link to="assignments/todo" className="block w-40 px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black" onClick={() => setIsOpen(false)}>Do wykonania</Link>

                  <Link to="assignments/done " className="block w-40 px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black" onClick={() => setIsOpen(false)}>Zrobione</Link>

                  <Link to="assignments/expired" className="block w-40 px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black" onClick={() => setIsOpen(false)}>Zaległe</Link>
                </div>
            ) : (<div
                className=' absolute left-16 z-10 w-56 mt-36 origin-top-right bg-white border border-hover_gray rounded-md shadow-lg pr-16'>

              <Link to="solutions/uncheck" className="block w-56 px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black" onClick={() => setIsOpen(false)}>Do sprawdzenia</Link>

              <Link to="solutions/check" className="block w-56 px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black" onClick={() => setIsOpen(false)}>Sprawdzone</Link>

              <Link to="solutions/late" className="block w-56 px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black" onClick={() => setIsOpen(false)}>Spóźnione</Link>

              <Link to="evaluation/reported" className="block w-56 px-4 py-2 text-sm text-black rounded-lg hover:bg-lilly-bg hover:text-black" onClick={() => setIsOpen(false)}>Zgłoszone oceny</Link>

            </div>))}
            {role === "Teacher" && <Link to="settings" onClick={() => setIsOpen(false)}>Ustawienia</Link>}

            {/*{role === "Student" ? (*/}
            {/*  <Link to="assignments/todo" className=''>Do wykonania</Link>*/}
            {/*) : (*/}
            {/*  <Link to="solutions/uncheck">Do sprawdzenia</Link>*/}
            {/*)}*/}
            {/*{role === "Student" ? (*/}
            {/*  <Link to="assignments/done">Zrobione</Link>*/}
            {/*) : (*/}
            {/*  <Link to="solutions/check">Sprawdzone</Link>*/}
            {/*)}*/}
            {/*{role === "Student" ? (*/}
            {/*  <Link to="assignments/expired">Zaległe</Link>*/}
            {/*) : (*/}
            {/*  <Link to="solutions/late">Spóźnione</Link>*/}
            {/*)}*/}

          </div>
        </header>
      </div>

    )
  }
  else{
    return <Loading></Loading>
  }
}

export default GroupHeader
