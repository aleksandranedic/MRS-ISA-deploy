import React, {useState} from 'react'
import {backLink, frontLink} from "../Consts";
import {SearchResultCard} from "./SearchResultCard";
import Pagination from "../Pagination";

const SearchResultsResources = ({list,name}) => {

    const [currentPage, setCurrentPage] = useState(1)

    const [postsPerPage] = useState(2)

    const indexOfLastVHPost = currentPage * postsPerPage;
    const indexOfFirstVHPost = indexOfLastVHPost - postsPerPage;
    const currentVHPost = list.slice(indexOfFirstVHPost, indexOfLastVHPost)

    const paginate = (number) => {
        setCurrentPage(number)
    }


    if (currentVHPost.length !== 0) {
        return <>{currentVHPost.map((item, index) =>
            (<SearchResultCard key={index} item={item}/>))}
            <Pagination key={name} paginate={paginate} postPerPage={postsPerPage}
                        totalPosts={list.length} name={name}/></>
    }
    return "";
}
export default SearchResultsResources