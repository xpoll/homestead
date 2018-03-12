<template>
  <header class="qxx_comp_header">
    <nav>
      <div>
        <router-link :to="'/'"><img class="float-left" src="../assets/lm.png"></router-link>
        <div class="logo-name ">千酌一梦醉独殇</div>
      </div>
      <ul>
        <li><router-link :to="'login'">Login</router-link></li>
        <li><router-link :to="'register'">Join</router-link></li>
      </ul>
    </nav>
  </header>
</template>

<style type="text/css" lang="scss">
.qxx_comp_header {
  // background-color: rgba(230, 56, 43, 0.5);
  nav {
    display: flex;
    justify-content: space-around;
    ul, div {
      display: flex;
    }

    ul {
      list-style: none;

      li a {
        color: #333333;
        padding: 5px 26px;
        font-size: 15px;
      }
    }
  }
  .logo-name {
    line-height: 4rem;
    font-size: 1.5rem;
    color: #ff8c3b;
    letter-spacing: 0.1rem;
  }
}
</style>
  