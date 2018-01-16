/*
 * Copyright (c) 2015 PocketHub
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.jzt.hol.android.jkda.sdk.page;

/**
 * Generic resource pager for elements with an id that can be paged
 *
 * @param <E>
 */
public abstract class ResourcePager<E> {

    /**
     * Next page to request
     */
    protected int currentPage = 1;

    /**
     * Number of pages to request
     */
    protected int pageCount = 1;

    protected int totalCount;

    private int pageSize = 10;

    public ResourcePager() {

    }

    public ResourcePager(int pageSize) {
        this.pageSize = pageSize;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
        pageCount = totalCount / pageSize;
        if(totalCount % pageSize > 0) {
            pageCount++;
        }
    }

    public boolean hasMore() {
        return currentPage < pageCount;
    }

    /**
     * clear all stored state
     *
     * @return this pager
     */
    public void reset() {
        currentPage = 1;
    }

}
