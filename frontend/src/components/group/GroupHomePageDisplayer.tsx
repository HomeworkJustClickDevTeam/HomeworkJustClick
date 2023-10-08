import { GroupInterface } from "../../types/GroupInterface"
import GroupListElement from "./GroupListElement"
import React from "react"

export const GroupHomePageDisplayer = (props: {groups: GroupInterface[]}) => {
  return (
    <div className='relative flex flex-wrap gap-x-5 gap-y-2 bg-light_gray px-8 py-6 bg-lilly-bg rounded-md'>
    {props.groups?.map((group) =>
      props.groups?.length > 0 ? <GroupListElement group={group} key={group.id}/> : <div>Dodaj nową grupę!</div>
    )}
  </div>)
}