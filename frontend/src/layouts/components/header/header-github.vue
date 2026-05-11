<template>
<base-hover-tip text="github地址">
  <div class="flex-center w-48px text-24px" @click="openGithub">
    <eg-icon icon="mdi:github" />
  </div>
</base-hover-tip>
</template>
<script setup lang="ts">

let githubUrl = "https://github.com/EnigmaGuest/fnk-server"

function openGithub() {
  window.open(githubUrl)
}
</script>

<style scoped>

</style>