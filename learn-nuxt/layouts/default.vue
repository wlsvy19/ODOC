<template>
    <div>
        <header>
            <h1>{{ $route.name }} 페이지</h1>
            <!-- <router-link></router-link> -->
            <!-- 아래와 동일 -->
            <nuxt-link to="/">홈페이지</nuxt-link>
            <nuxt-link to="/main">메인 페이지</nuxt-link>
            <nuxt-link to="/product">상품 페이지</nuxt-link>
        </header>
        <nuxt /> <!-- <router-view/> -->
    </div>
</template>
